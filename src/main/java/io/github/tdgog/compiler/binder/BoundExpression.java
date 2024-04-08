package io.github.tdgog.compiler.binder;

public abstract class BoundExpression extends BoundNode {

    // TODO: 04/11/2023 Should return Type when Type is created
    public abstract Class<?> getType();

}
