package io.github.tdgog.compiler.exceptions;

import io.github.tdgog.compiler.binder.binary.BoundBinaryOperatorKind;

public class UnexpectedBinaryOperatorException extends Exception {
    public UnexpectedBinaryOperatorException(BoundBinaryOperatorKind kind) {
        super("Unexpected binary operator " + kind);
    }
}
