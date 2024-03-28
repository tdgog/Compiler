package io.github.tdgog.compiler.binder.literal;

import io.github.tdgog.compiler.binder.BoundExpression;
import io.github.tdgog.compiler.binder.BoundNodeKind;
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
