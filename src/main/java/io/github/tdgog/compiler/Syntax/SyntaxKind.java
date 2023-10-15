package io.github.tdgog.compiler.Syntax;

/**
 * The types of tokens and expressions in the language.
 */
public enum SyntaxKind {
    StringToken,
    IntegerToken,
    PlusToken,
    MinusToken,
    TimesToken,
    DivideToken,
    OpenBracketToken,
    CloseBracketToken,
    BadToken,
    EOFToken,
    IntegerExpression,
    BinaryExpression,
    WhitespaceToken
}
