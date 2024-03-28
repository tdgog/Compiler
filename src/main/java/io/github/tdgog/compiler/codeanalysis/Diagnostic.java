package io.github.tdgog.compiler.codeanalysis;

import io.github.tdgog.compiler.text.TextSpan;

public record Diagnostic(TextSpan textSpan, String content) {
    @Override
    public String toString() {
        return content;
    }
}
