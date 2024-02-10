package io.github.tdgog.compiler.CodeAnalysis;

import io.github.tdgog.compiler.Text.TextSpan;

public record Diagnostic(TextSpan textSpan, String content) {
    @Override
    public String toString() {
        return content;
    }
}
