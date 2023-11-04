package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Syntax.Expressions.*;
import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import io.github.tdgog.compiler.Syntax.SyntaxTree;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Parser {

    private final SyntaxToken[] tokens;
    private int position;
    @Getter
    private final ArrayList<String> diagnostics = new ArrayList<>();

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

        diagnostics.add("ERROR: Unexpected token <" + getCurrent().getSyntaxKind() + ">, expected <" + kind + ">.");
        return new SyntaxToken(kind, getCurrent().getPosition(), null, null);
    }

    private SyntaxToken match(SyntaxKind[] kinds) {
        for (SyntaxKind kind : kinds) {
            if (getCurrent().getSyntaxKind() == kind) {
                return nextToken();
            }
        }

        StringJoiner expectedKinds = new StringJoiner("|");
        for (SyntaxKind kind : kinds) {
            expectedKinds.add("<" + kind + ">");
        }

        diagnostics.add("ERROR: Unexpected token <" + getCurrent().getSyntaxKind() + ">, expected " + expectedKinds + ".");
        return new SyntaxToken(kinds[0], getCurrent().getPosition(), null, null);
    }

    /**
     * Parses the tokens into an expression tree
     * @return The expression tree
     */
    public SyntaxTree parse() {
        ExpressionSyntax expression = parseTerm();
        SyntaxToken eofToken = match(SyntaxKind.EOFToken);
        return new SyntaxTree(expression, eofToken, diagnostics);
    }

    /**
     * Parses addition and subtraction
     * @return The parsed expression
     */
    @NotNull
    private ExpressionSyntax parseTerm() {
        ExpressionSyntax left = parseFactor();
        while (getCurrent().getSyntaxKind() == SyntaxKind.PlusToken
                || getCurrent().getSyntaxKind() == SyntaxKind.MinusToken) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseFactor();
            left = new BinaryExpressionSyntax(left, operatorToken, right);
        }
        return left;
    }

    /**
     * Parses multiplication and division
     * @return The parsed expression
     */
    @NotNull
    private ExpressionSyntax parseFactor() {
        ExpressionSyntax left = parsePrimaryExpression();
        while (getCurrent().getSyntaxKind() == SyntaxKind.MultiplyToken
                || getCurrent().getSyntaxKind() == SyntaxKind.DivideToken
                || getCurrent().getSyntaxKind() == SyntaxKind.ModuloToken) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parsePrimaryExpression();
            left = new BinaryExpressionSyntax(left, operatorToken, right);
        }
        return left;
    }

    /**
     * Parses a primary expression such as a constant or identifier
     * @return The primary expression
     */
    private @NotNull ExpressionSyntax parsePrimaryExpression() {
        if (getCurrent().getSyntaxKind() == SyntaxKind.OpenBracketToken) {
            SyntaxToken openBracketToken = nextToken();
            ExpressionSyntax expression = parseTerm();
            SyntaxToken closeBracketToken = match(SyntaxKind.CloseBracketToken);
            return new BracketExpressionSyntax(openBracketToken, expression, closeBracketToken);
        }

        SyntaxToken token = match(new SyntaxKind[]{SyntaxKind.IntegerToken, SyntaxKind.FloatToken});
        return switch (token.getSyntaxKind()) {
            case IntegerToken, FloatToken -> new LiteralExpressionSyntax(token);
            // Temp
            default -> new LiteralExpressionSyntax(token);
        };

    }

}
