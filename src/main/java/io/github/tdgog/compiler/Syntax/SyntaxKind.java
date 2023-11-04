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
    LiteralExpression
}
