package io.github.tdgog.compiler.TreeParser.Syntax;

import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.Text.SourceText;
import io.github.tdgog.compiler.TreeParser.Parser;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.CompilationUnitSyntax;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a tree of syntax tokens
 */
@Getter
@AllArgsConstructor
public class SyntaxTree {

    private final SourceText text;
    private final CompilationUnitSyntax root;
    private final DiagnosticCollection diagnostics;

    public SyntaxTree(SourceText text) {
        Parser parser = new Parser(text);

        this.text = text;
        root = parser.parseCompilationUnit();
        diagnostics = parser.getDiagnostics();


    }

    public static SyntaxTree parse(String text) {
        return parse(new SourceText(text));
    }

    public static SyntaxTree parse(SourceText text) {
        return new SyntaxTree(text);
    }

}
