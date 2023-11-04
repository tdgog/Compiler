package io.github.tdgog.compiler.Syntax;

/**
 * The types of tokens and expressions in the language.
 */
public enum SyntaxKind {
    // Tokens
    BadToken,
    EOFToken,
    WhitespaceToken,
    IntegerToken,
    FloatToken,
    PlusToken,
    MinusToken,
    MultiplyToken,
    DivideToken,
    ModuloToken,
    OpenBracketToken,
    CloseBracketToken,

    // Expressions
    BinaryExpression,
    BracketExpression,
    LiteralExpression;

    /**
     * Returns an integer representing the position in the precedence hierarchy.
     * Higher integers should be executed first.
     * @return The precedence of the operator
     */
    public int getBinaryOperatorPrecedence() {
        return switch (this) {
            case PlusToken, MinusToken -> 1;
            case MultiplyToken, DivideToken, ModuloToken -> 2;
            default -> 0;
        };
    }

}
