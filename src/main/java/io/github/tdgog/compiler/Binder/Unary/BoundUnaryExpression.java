package io.github.tdgog.compiler.Binder.Unary;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.Binder.BoundNodeKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundUnaryExpression extends BoundExpression {

    private final BoundUnaryOperator operator;
    private final BoundExpression operand;

    @Override
    public Class<?> getType() {
        return operand.getType();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.UnaryExpression;
    }

}
