package io.github.tdgog.compiler.Binder.Scope;

import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.CodeAnalysis.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoundGlobalScope {

    private final BoundGlobalScope previous;
    private final DiagnosticCollection diagnostics;
    private final List<VariableSymbol> variables;
    private final BoundExpression expression;

}
