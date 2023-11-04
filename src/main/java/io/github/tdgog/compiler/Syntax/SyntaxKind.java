package io.github.tdgog.compiler.Syntax;

/**
 * The types of tokens and expressions in the language.
 */
public enum SyntaxKind {
    IntegerToken,
    FloatToken,
    PlusToken,
    MinusToken,
    MultiplyToken,
    DivideToken,
    OpenBracketToken,
    CloseBracketToken,
    BadToken,
    EOFToken,
    BinaryExpression,
    BracketExpression,
    ModuloToken,
    LiteralExpression,
    WhitespaceToken
}
