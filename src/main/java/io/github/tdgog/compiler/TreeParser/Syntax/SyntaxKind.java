package io.github.tdgog.compiler.TreeParser.Syntax;

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
    IdentifierToken,

    // Expressions
    UnaryExpression,
    BinaryExpression,
    BracketExpression,
    LiteralExpression,

    // Keywords
    TrueKeyword,
    FalseKeyword;

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

    /**
     * Returns an integer representing the position in the precedence hierarchy.
     * Higher integers should be executed first.
     * @return The precedence of the operator
     */
    public int getUnaryOperatorPrecedence() {
        return switch (this) {
            case PlusToken, MinusToken -> 3;
            default -> 0;
        };
    }

    public static SyntaxKind getKeywordKind(String string) {
        return switch (string) {
            case "true" -> SyntaxKind.TrueKeyword;
            case "false" -> SyntaxKind.FalseKeyword;
            default -> SyntaxKind.IdentifierToken;
        };
    }

}
