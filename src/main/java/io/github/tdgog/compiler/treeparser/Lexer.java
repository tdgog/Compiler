package io.github.tdgog.compiler.treeparser;

import io.github.tdgog.compiler.codeanalysis.DiagnosticCollection;
import io.github.tdgog.compiler.text.SourceText;
import io.github.tdgog.compiler.text.TextSpan;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Lexer {

    private final SourceText text;
    private int position;
    @Getter
    private final DiagnosticCollection diagnostics = new DiagnosticCollection();
    
    public Lexer(SourceText text) {
        this.text = text;
    }

    /**
     * Move to the next token
     */
    private void next() {
        position++;
    }

    /**
     * Get the current character
     * @return The current character or a null character if the end of the expression has been reached
     */
    private char getCurrentCharacter() {
        return getCharacterAtPosition(position);
    }

    /**
     * Get the character a number of indexes forward in the text
     * @param offset The number of indexes forward
     * @return The character the number of indexes forward or a null character if the end of the expression has been reached
     */
    private char peek(int offset) {
        return getCharacterAtPosition(position + offset);
    }

    /**
     * Get the character at a specific index in the text
     * @param position The position of the character
     * @return The character at the position or a null character if the end of the expression has been reached
     */
    private char getCharacterAtPosition(int position) {
        if (text == null) {
            throw new RuntimeException("Attempt to parse a null expression.");
        }

        if (position >= text.length())
            return '\0';

        return text.charAt(position);
    }

    /**
     * Get the next syntax token
     * @return The next syntax token
     */
    public SyntaxToken lex() {
        // If the end of the expression has been reached, return an EOF token
        if (position >= text.length())
            return new SyntaxToken(SyntaxKind.EOFToken, position, "\0");

        // If the current character is a digit, return a number token
        if (Character.isDigit(getCurrentCharacter())
                || (getCurrentCharacter() == '.' && Character.isDigit(getCharacterAtPosition(position + 1)))) {

            SyntaxToken numberToken = readNumber();
            if (numberToken != null) return numberToken;
        }

        // If the current character is whitespace, return a whitespace token
        if (Character.isWhitespace(getCurrentCharacter())) {
            return readWhitespace();
        }

        if (Character.isLetter(getCurrentCharacter())) {
            return readIdentifierOrKeyword();
        }

        // If the current character is a symbol, return the token for that symbol
        switch (getCurrentCharacter()) {
            case '+':
                return new SyntaxToken(SyntaxKind.PlusToken, position++, "+");
            case '-':
                return new SyntaxToken(SyntaxKind.MinusToken, position++, "-");
            case '*':
                return new SyntaxToken(SyntaxKind.MultiplyToken, position++, "*");
            case '/':
                return new SyntaxToken(SyntaxKind.DivideToken, position++, "/");
            case '(':
                return new SyntaxToken(SyntaxKind.OpenBracketToken, position++, "(");
            case ')':
                return new SyntaxToken(SyntaxKind.CloseBracketToken, position++, ")");
            case '%':
                return new SyntaxToken(SyntaxKind.ModuloToken, position++, "%");
            case '&':
                if (peek(1) == '&') {
                    SyntaxToken token = new SyntaxToken(SyntaxKind.DoubleAmpersandToken, position, "&&");
                    position += 2;
                    return token;
                }
                break;
            case '|':
                if (peek(1) == '|') {
                    SyntaxToken token = new SyntaxToken(SyntaxKind.DoublePipeToken, position, "||");
                    position += 2;
                    return token;
                }
                break;
            case '=':
                if (peek(1) == '=') {
                    SyntaxToken token = new SyntaxToken(SyntaxKind.DoubleEqualsToken, position, "==");
                    position += 2;
                    return token;
                }
                return new SyntaxToken(SyntaxKind.EqualsToken, position++, "=");
            case '!':
                if (peek(1) == '=') {
                    SyntaxToken token = new SyntaxToken(SyntaxKind.BangEqualsToken, position, "!=");
                    position += 2;
                    return token;
                }
                return new SyntaxToken(SyntaxKind.BangToken, position++, "!");
        }

        // Return a bad token if the current character is not part of a valid token
        diagnostics.reportBadCharacter(position, getCurrentCharacter());
        return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position - 1, position));
    }

    @Nullable
    private SyntaxToken readNumber() {
        boolean dotFound = false;
        int start = position;
        while (Character.isDigit(getCurrentCharacter())
                || (getCurrentCharacter() == '.' && Character.isDigit(getCharacterAtPosition(position + 1)))) {
            // Only permit a single decimal character
            if (getCurrentCharacter() == '.') {
                if (dotFound) {
                    next();
                    break;
                }
                dotFound = true;
            }

            next();
        }

        int length = position - start;
        String value = text.substring(start, start + length);

        if (TokenDatatypeChecker.isInteger(value))
            return new SyntaxToken(SyntaxKind.IntegerToken, start, value, Integer.parseInt(value));
        else if (TokenDatatypeChecker.isFloat(value))
            return new SyntaxToken(SyntaxKind.FloatToken, start, value, Double.parseDouble(value));

        diagnostics.reportInvalidNumber(new TextSpan(start, length), value, Double.class);
        return null;
    }

    @NotNull
    private SyntaxToken readWhitespace() {
        int start = position;
        while (Character.isWhitespace(getCurrentCharacter())) {
            next();
        }

        int length = position - start;
        String value = text.substring(start, start + length);

        return new SyntaxToken(SyntaxKind.WhitespaceToken, start, value);
    }

    @NotNull
    private SyntaxToken readIdentifierOrKeyword() {
        int start = position;
        while (Character.isLetter(getCurrentCharacter())) {
            next();
        }

        int length = position - start;
        String value = text.substring(start, start + length);
        SyntaxKind syntaxKind = SyntaxKind.getKeywordKind(value);
        return new SyntaxToken(syntaxKind, start, value);
    }

}
