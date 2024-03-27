package io.github.tdgog.compiler.Evaluation;

import java.util.Objects;

public record Pair(Class<?> left, Class<?> right) {

    public boolean equals(Pair other) {
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    public boolean equals(Class<?> other) {
        return Objects.equals(left, other) && Objects.equals(right, other);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other instanceof Pair pair)
            return equals(pair);

        return false;
    }

    public boolean contains(Class<?> other) {
        return Objects.equals(left, other) || Objects.equals(right, other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
