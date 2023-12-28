package io.github.tdgog.compiler.Binder.Named;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.Binder.BoundNodeKind;
import io.github.tdgog.compiler.CodeAnalysis.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoundVariableExpression extends BoundExpression {

    private final VariableSymbol variable;

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.VariableExpression;
    }

    @Override
    public Class<?> getType() {
        return variable.type();
    }
}
