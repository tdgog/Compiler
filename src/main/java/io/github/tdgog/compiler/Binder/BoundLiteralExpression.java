package io.github.tdgog.compiler.Binder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class BoundLiteralExpression extends BoundExpression {

    private final Object value;

    @Override
    public Object getType() {
        return value.getClass();
    }

    @Override
    public BoundNodeKind getBoundNodeKind() {
        return BoundNodeKind.LiteralExpression;
    }
}
