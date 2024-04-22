package io.github.tdgog.compiler.evaluation;

import io.github.tdgog.compiler.codeanalysis.DiagnosticCollection;

public record EvaluationResult(DiagnosticCollection diagnostics, Object value) {
}
