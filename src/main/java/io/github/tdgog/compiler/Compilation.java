package io.github.tdgog.compiler;

import io.github.tdgog.compiler.binder.Binder;
import io.github.tdgog.compiler.binder.scope.BoundGlobalScope;
import io.github.tdgog.compiler.codeanalysis.DiagnosticCollection;
import io.github.tdgog.compiler.codeanalysis.VariableCollection;
import io.github.tdgog.compiler.evaluation.EvaluationResult;
import io.github.tdgog.compiler.evaluation.Evaluator;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxTree;
import lombok.Getter;

@Getter
public class Compilation {

    private final SyntaxTree syntaxTree;
    private final Compilation previous;
    private final BoundGlobalScope globalScope;

    public Compilation(SyntaxTree syntaxTree, Compilation previous) {
        this.syntaxTree = syntaxTree;
        this.previous = previous;
        globalScope = Binder.bindGlobalScope(previous, syntaxTree);
    }

    public Compilation(SyntaxTree syntaxTree) {
        this(syntaxTree, null);
    }

    public EvaluationResult evaluate(String line, VariableCollection variables) {
        DiagnosticCollection diagnostics = DiagnosticCollection.createFrozen(
                syntaxTree.getDiagnostics(),
                globalScope.getDiagnostics());
        diagnostics.setSource(syntaxTree.getText());
        diagnostics.print(line);

        // Evaluate the syntax tree
        if (diagnostics.isEmpty()) {
            Evaluator evaluator = new Evaluator(globalScope.getExpression(), variables);
            return new EvaluationResult(diagnostics, evaluator.evaluate());
        }
        return new EvaluationResult(diagnostics, null);
    }

    public Compilation continueWith(SyntaxTree syntaxTree) {
        return new Compilation(syntaxTree, this);
    }

}
