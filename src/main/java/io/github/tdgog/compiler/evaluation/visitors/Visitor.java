package io.github.tdgog.compiler.evaluation.visitors;

import io.github.tdgog.compiler.binder.binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.evaluation.Pair;
import io.github.tdgog.compiler.exceptions.UnexpectedBinaryOperatorException;

import java.util.function.BiFunction;

public interface Visitor {

    BiFunction<Object, Object, Object> getMethod(Pair arguments);

    BoundBinaryOperatorKind getKind();

    default boolean acceptsOperator(BoundBinaryOperatorKind operatorKind) {
        return operatorKind == getKind();
    }

    default Object visit(Object left, Object right) throws UnexpectedBinaryOperatorException {
        BiFunction<Object, Object, Object> method = getMethod(new Pair(left.getClass(), right.getClass()));
        if (method != null)
            return method.apply(left, right);
        throw new UnexpectedBinaryOperatorException(getKind());
    }

}
