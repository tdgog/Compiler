package io.github.tdgog.compiler.Binder.Named;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.Binder.BoundNodeKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoundVariableExpression extends BoundExpression {

    private final String name;
    private final Class<?> type;

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.VariableExpression;
    }
}
