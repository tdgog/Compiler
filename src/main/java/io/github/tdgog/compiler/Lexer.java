package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxToken;

public class Lexer {

    private final String text;
    private int position;
    
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
        if (Character.isDigit(getCurrentCharacter())) {
            int start = position;
            while (Character.isDigit(getCurrentCharacter())) {
                next();
            }

            int length = position - start;
            String value = text.substring(start, start + length);

            if (TokenDatatypeChecker.isInteger(value))
                return new SyntaxToken(SyntaxKind.IntegerToken, start, value, Integer.parseInt(value));
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
        if (getCurrentCharacter() == '+')
            return new SyntaxToken(SyntaxKind.PlusToken, position++, "+", null);
        if (getCurrentCharacter() == '-')
            return new SyntaxToken(SyntaxKind.MinusToken, position++, "-", null);
        if (getCurrentCharacter() == '*')
            return new SyntaxToken(SyntaxKind.TimesToken, position++, "*", null);
        if (getCurrentCharacter() == '/')
            return new SyntaxToken(SyntaxKind.DivideToken, position++, "/", null);
        if (getCurrentCharacter() == '(')
            return new SyntaxToken(SyntaxKind.OpenBracketToken, position++, "(", null);
        if (getCurrentCharacter() == ')')
            return new SyntaxToken(SyntaxKind.CloseBracketToken, position++, ")", null);

        // Return a bad token if the current character is not part of a valid token
        return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position - 1, position), null);
    }

}