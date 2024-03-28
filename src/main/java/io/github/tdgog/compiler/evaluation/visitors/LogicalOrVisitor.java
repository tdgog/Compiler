package io.github.tdgog.compiler.evaluation.visitors;

import io.github.tdgog.compiler.binder.binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.evaluation.Pair;

import java.util.function.BiFunction;

import static io.github.tdgog.compiler.evaluation.TypeCaster.toBoolean;

public class LogicalOrVisitor implements Visitor {

    BiFunction<Object, Object, Object> operation = (a, b) -> toBoolean(a) || toBoolean(b);

    @Override
    public BiFunction<Object, Object, Object> getMethod(Pair arguments) {
        return operation;
    }

    @Override
    public BoundBinaryOperatorKind getKind() {
        return BoundBinaryOperatorKind.LogicalOr;
    }

}
