package io.github.tdgog.compiler.binder.binary;

import io.github.tdgog.compiler.binder.BoundExpression;
import io.github.tdgog.compiler.binder.BoundNodeKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundBinaryExpression extends BoundExpression {

    private final BoundExpression left;
    private final BoundBinaryOperator operator;
    private final BoundExpression right;

    @Override
    public Class<?> getType() {
        return operator.getResultType();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.BinaryExpression;
    }

}
