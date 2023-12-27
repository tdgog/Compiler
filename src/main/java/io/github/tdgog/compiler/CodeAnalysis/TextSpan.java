package io.github.tdgog.compiler.CodeAnalysis;

public record TextSpan(int start, int length) {

    public int end() {
        return start + length;
    }

}
