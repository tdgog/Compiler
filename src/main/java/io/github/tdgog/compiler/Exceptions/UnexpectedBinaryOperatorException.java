package io.github.tdgog.compiler.Exceptions;

import io.github.tdgog.compiler.Binder.Binary.BoundBinaryOperatorKind;

public class UnexpectedBinaryOperatorException extends Exception {
    public UnexpectedBinaryOperatorException(BoundBinaryOperatorKind kind) {
        super("Unexpected binary operator " + kind);
    }
}
