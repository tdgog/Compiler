package io.github.tdgog.compiler.CodeAnalysis;

public record Diagnostic(TextSpan textSpan, String content) {
    @Override
    public String toString() {
        return content;
    }
}
