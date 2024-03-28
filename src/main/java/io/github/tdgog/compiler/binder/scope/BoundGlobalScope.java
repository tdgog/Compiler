package io.github.tdgog.compiler.binder.scope;

import io.github.tdgog.compiler.binder.BoundExpression;
import io.github.tdgog.compiler.codeanalysis.DiagnosticCollection;
import io.github.tdgog.compiler.codeanalysis.VariableSymbol;
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
