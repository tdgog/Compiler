package io.github.tdgog.compiler.binder.named;

import io.github.tdgog.compiler.binder.BoundExpression;
import io.github.tdgog.compiler.binder.BoundNodeKind;
import io.github.tdgog.compiler.codeanalysis.VariableSymbol;
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
