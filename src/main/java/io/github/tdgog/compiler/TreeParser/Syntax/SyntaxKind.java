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
    BangToken,
    DoubleAmpersandToken,
    DoublePipeToken,
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
            case DoublePipeToken -> 1;
            case DoubleAmpersandToken -> 2;
            case PlusToken, MinusToken -> 3;
            case MultiplyToken, DivideToken, ModuloToken -> 4;
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
            case PlusToken, MinusToken, BangToken -> Integer.MAX_VALUE;
            default -> 0;
        };
    }

    /**
     * Gets the SyntaxKind for a given keyword
     * @param string The keyword
     * @return The SyntaxKind
     */
    public static SyntaxKind getKeywordKind(String string) {
        return switch (string) {
            case "true" -> SyntaxKind.TrueKeyword;
            case "false" -> SyntaxKind.FalseKeyword;
            default -> SyntaxKind.IdentifierToken;
        };
    }

}
