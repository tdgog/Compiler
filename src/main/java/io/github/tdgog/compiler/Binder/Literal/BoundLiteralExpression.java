package io.github.tdgog.compiler.Binder.Literal;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.Binder.BoundNodeKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundLiteralExpression extends BoundExpression {

    private final Object value;

    @Override
    public Class<?> getType() {
        return value.getClass();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.LiteralExpression;
    }
}
