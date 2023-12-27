package io.github.tdgog.compiler.Evaluation.Visitors;

import io.github.tdgog.compiler.Binder.Binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.Evaluation.Pair;

import java.util.function.BiFunction;

import static io.github.tdgog.compiler.Evaluation.TypeCaster.toBoolean;

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
