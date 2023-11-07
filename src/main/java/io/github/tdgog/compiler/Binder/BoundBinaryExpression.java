package io.github.tdgog.compiler.Binder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundBinaryExpression extends BoundExpression {

    private final BoundExpression left;
    private final BoundBinaryOperatorKind operatorKind;
    private final BoundExpression right;

    @Override
    public Object getType() {
        return left.getType();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.BinaryExpression;
    }

}
