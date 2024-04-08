package io.github.tdgog.compiler.binder.named;

import io.github.tdgog.compiler.binder.BoundExpression;
import io.github.tdgog.compiler.binder.BoundNodeKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoundAssignmentExpression extends BoundExpression {

    private final String name;
    private final BoundExpression expression;

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.AssignmentExpression;
    }

    @Override
    public Class<?> getType() {
        return expression.getType();
    }

}
