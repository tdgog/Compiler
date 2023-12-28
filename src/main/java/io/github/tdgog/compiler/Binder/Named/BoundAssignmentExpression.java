package io.github.tdgog.compiler.Binder.Named;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.Binder.BoundNodeKind;
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
