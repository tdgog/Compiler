package io.github.tdgog.compiler.Binder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundUnaryExpression extends BoundExpression {

    private final BoundUnaryOperatorKind operatorKind;
    private final BoundExpression operand;

    @Override
    public Object getType() {
        return operand.getType();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.UnaryExpression;
    }

}
