package io.github.tdgog.compiler.TreeParser;

import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.*;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxToken;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxTree;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Parser {

    private final SyntaxToken[] tokens;
    private int position;
    @Getter
    private final DiagnosticCollection diagnostics = new DiagnosticCollection();

    public Parser(String text) {
        this.tokens = lex(text);
    }

    /**
     * Runs the lexer on the text and returns the tokens as an array
     * @param text The text to be lexed
     * @return The tokens
     */
    private SyntaxToken @NotNull [] lex(String text) {
        ArrayList<SyntaxToken> tokens = new ArrayList<>();

        Lexer lexer = new Lexer(text);
        SyntaxToken token;
        do {
            token = lexer.lex();

            if (token.getSyntaxKind() != SyntaxKind.WhitespaceToken
                    && token.getSyntaxKind() != SyntaxKind.BadToken)
                tokens.add(token);

        } while (token.getSyntaxKind() != SyntaxKind.EOFToken);

        diagnostics.addAll(lexer.getDiagnostics());
        return tokens.toArray(new SyntaxToken[0]);
    }

    /**
     * Returns the token a number of positions forward of the current one, or the last token
     * @param offset The number of positions to look forward
     * @return The token
     */
    private SyntaxToken peek(int offset) {
        int index = position + offset;

        if (index >= tokens.length)
            return tokens[tokens.length - 1];

        return tokens[index];
    }

    /**
     * Returns the current token
     * @return The current token
     */
    private SyntaxToken getCurrent() {
        return peek(0);
    }

    /**
     * Returns the current token and moves the position forward
     * @return The current token
     */
    private SyntaxToken nextToken() {
        SyntaxToken current = getCurrent();
        position++;
        return current;
    }

    /**
     * Returns the current token if it matches the given kind, or a new empty token with the given kind
     * @param kind The kind of token to match
     * @return A token
     */
    private SyntaxToken match(SyntaxKind kind) {
        if (getCurrent().getSyntaxKind() == kind)
            return nextToken();

        diagnostics.reportUnexpectedToken(getCurrent(), kind);
        return new SyntaxToken(kind, getCurrent().getPosition(), null);
    }

    private SyntaxToken match(SyntaxKind[] kinds) {
        for (SyntaxKind kind : kinds)
            if (getCurrent().getSyntaxKind() == kind)
                return nextToken();

        diagnostics.reportUnexpectedToken(getCurrent(), kinds);
        return new SyntaxToken(kinds[0], getCurrent().getPosition(), null);
    }

    /**
     * Parses the tokens into an expression tree
     * @return The expression tree
     */
    public SyntaxTree parse() {
        ExpressionSyntax expression = parseExpression();
        SyntaxToken eofToken = match(SyntaxKind.EOFToken);
        return new SyntaxTree(expression, eofToken, diagnostics);
    }

    private ExpressionSyntax parseExpression() {
        return parseAssignmentExpression();
    }

    private ExpressionSyntax parseAssignmentExpression() {
        if (getCurrent().getSyntaxKind() == SyntaxKind.IdentifierToken
                && peek(1).getSyntaxKind() == SyntaxKind.EqualsToken) {
            SyntaxToken identifier = nextToken();
            SyntaxToken operator = nextToken();
            ExpressionSyntax right = parseAssignmentExpression();
            return new AssignmentExpressionSyntax(identifier, operator, right);
        }

        return parseBinaryExpression();
    }

    /**
     * Parses an expression
     * @param parentPrecedence The operator's precedence {@link io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind#getBinaryOperatorPrecedence()}
     * @return The expression
     */
    private ExpressionSyntax parseBinaryExpression(int parentPrecedence) {
        // Parse unary expressions
        ExpressionSyntax left;
        int unaryOperatorPrecedence = getCurrent().getSyntaxKind().getUnaryOperatorPrecedence();
        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence)
            left = new UnaryExpressionSyntax(nextToken(), parseBinaryExpression(unaryOperatorPrecedence));
        else
            left = parsePrimaryExpression();

        // Parse binary expressions
        while (true) {
            int precedence = getCurrent().getSyntaxKind().getBinaryOperatorPrecedence();
            if (precedence == 0 || precedence <= parentPrecedence)
                break;

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseBinaryExpression(precedence);
            left = new BinaryExpressionSyntax(left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseBinaryExpression() {
        return parseBinaryExpression(0);
    }

    /**
     * Parses a primary expression such as a constant or identifier
     * @return The primary expression
     */
    private @NotNull ExpressionSyntax parsePrimaryExpression() {
        switch (getCurrent().getSyntaxKind()) {
            case OpenBracketToken -> {
                SyntaxToken openBracketToken = nextToken();
                ExpressionSyntax expression = parseBinaryExpression();
                SyntaxToken closeBracketToken = match(SyntaxKind.CloseBracketToken);
                return new BracketExpressionSyntax(openBracketToken, expression, closeBracketToken);
            }
            case TrueKeyword, FalseKeyword -> {
                Boolean value = getCurrent().getSyntaxKind() == SyntaxKind.TrueKeyword;
                return new LiteralExpressionSyntax(nextToken(), value);
            }
            case IdentifierToken -> {
                SyntaxToken identifierToken = nextToken();
                return new NameExpressionSyntax(identifierToken);
            }
            default -> {
                SyntaxToken token = match(new SyntaxKind[]{SyntaxKind.IntegerToken, SyntaxKind.FloatToken});
                return switch (token.getSyntaxKind()) {
                    case IntegerToken, FloatToken -> new LiteralExpressionSyntax(token);
                    // Temp
                    default -> new LiteralExpressionSyntax(token);
                };
            }
        }
    }

}
