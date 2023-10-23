package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import lombok.Getter;

import java.util.ArrayList;

public class Lexer {

    private final String text;
    private int position;
    @Getter
    private ArrayList<String> diagnostics = new ArrayList<>();
    
    public Lexer(String text) {
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
    public SyntaxToken nextToken() {
        // If the end of the expression has been reached, return an EOF token
        if (position >= text.length())
            return new SyntaxToken(SyntaxKind.EOFToken, position, "\0", null);

        // If the current character is a digit, return a number token
        if (Character.isDigit(getCurrentCharacter())
                || (getCurrentCharacter() == '-' && Character.isDigit(getCharacterAtPosition(position + 1)))
                || (getCurrentCharacter() == '.' && Character.isDigit(getCharacterAtPosition(position + 1)))) {

            boolean dotFound = false;
            int start = position;
            while (Character.isDigit(getCurrentCharacter())
                    || (getCurrentCharacter() == '-' && Character.isDigit(getCharacterAtPosition(position + 1)))
                    || (getCurrentCharacter() == '.' && Character.isDigit(getCharacterAtPosition(position + 1)))) {
                // Only permit a single decimal character
                if (getCurrentCharacter() == '.') {
                    if (dotFound) {
                        next();
                        break;
                    }
                    dotFound = true;
                }

                // Only permit a single minus sign and only let that be at the start
                if (getCurrentCharacter() == '-' && start != position) {
                    next();
                    break;
                }

                next();
            }

            int length = position - start;
            String value = text.substring(start, start + length);

            if (TokenDatatypeChecker.isInteger(value))
                return new SyntaxToken(SyntaxKind.IntegerToken, start, value, Integer.parseInt(value));
            else if (TokenDatatypeChecker.isFloat(value))
                return new SyntaxToken(SyntaxKind.FloatToken, start, value, Double.parseDouble(value));
            else
                diagnostics.add("ERROR: '" + value + "' is not a valid number");
        }

        // If the current character is whitespace, return a whitespace token
        if (Character.isWhitespace(getCurrentCharacter())) {
            int start = position;
            while (Character.isWhitespace(getCurrentCharacter())) {
                next();
            }

            int length = position - start;
            String value = text.substring(start, start + length);

            return new SyntaxToken(SyntaxKind.WhitespaceToken, start, value, null);
        }

        // If the current character is a symbol, return the token for that symbol
        switch (getCurrentCharacter()) {
            case '+':
                return new SyntaxToken(SyntaxKind.PlusToken, position++, "+", null);
            case '-':
                return new SyntaxToken(SyntaxKind.MinusToken, position++, "-", null);
            case '*':
                return new SyntaxToken(SyntaxKind.MultiplyToken, position++, "*", null);
            case '/':
                return new SyntaxToken(SyntaxKind.DivideToken, position++, "/", null);
            case '(':
                return new SyntaxToken(SyntaxKind.OpenBracketToken, position++, "(", null);
            case ')':
                return new SyntaxToken(SyntaxKind.CloseBracketToken, position++, ")", null);
            case '%':
                return new SyntaxToken(SyntaxKind.ModuloToken, position++, "%", null);
        }

        // Return a bad token if the current character is not part of a valid token
        diagnostics.add("ERROR: Bad character input: '" + getCurrentCharacter() + "'");
        return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position - 1, position), null);
    }

}