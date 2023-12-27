package io.github.tdgog.compiler.TreeParser.Syntax;

import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.TreeParser.Parser;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.ExpressionSyntax;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a tree of syntax tokens
 */
@Getter
@AllArgsConstructor
public class SyntaxTree {

    private final ExpressionSyntax root;
    private final SyntaxToken eofToken;
    private final DiagnosticCollection diagnostics;

    public static SyntaxTree parse(String text) {
        return new Parser(text).parse();
    }

}
