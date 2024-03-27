package io.github.tdgog.compiler.Evaluation.Visitors;

import io.github.tdgog.compiler.Binder.Binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.Evaluation.Pair;

import java.util.Map;
import java.util.function.BiFunction;

import static io.github.tdgog.compiler.Evaluation.TypeCaster.toDouble;
import static java.util.Map.entry;

public class AdditionVisitor implements Visitor {

    private static final Map<Pair, BiFunction<Object, Object, Object>> operations = Map.ofEntries(
            entry(new Pair(Integer.class, Integer.class), (a, b) -> (int) a + (int) b),
            entry(new Pair(Double.class, Double.class), (a, b) -> toDouble(a) + toDouble(b))
    );

    @Override
    public BiFunction<Object, Object, Object> getMethod(Pair arguments) {
        if (arguments.equals(Integer.class))
            return operations.get(arguments);
        if (arguments.contains(Double.class))
            return operations.get(new Pair(Double.class, Double.class));
        return null;
    }

    @Override
    public BoundBinaryOperatorKind getKind() {
        return BoundBinaryOperatorKind.Addition;
    }

}
