package io.github.tdgog.compiler.Syntax;

/**
 * The types of tokens and expressions in the language.
 */
public enum SyntaxKind {
    StringToken,
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
    IntegerExpression,
    FloatExpression,
    BinaryExpression,
    BracketExpression,
    ModuloToken, LiteralExpression, WhitespaceToken
}
