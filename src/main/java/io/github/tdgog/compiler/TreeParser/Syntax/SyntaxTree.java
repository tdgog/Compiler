package io.github.tdgog.compiler.TreeParser.Syntax;

import io.github.tdgog.compiler.TreeParser.Parser;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.ExpressionSyntax;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Represents a tree of syntax tokens
 */
@Getter
public class SyntaxTree {

    private final ExpressionSyntax root;
    private final SyntaxToken eofToken;
    private final List<String> diagnostics;

    public SyntaxTree(ExpressionSyntax root, SyntaxToken eofToken, List<String> diagnostics) {
        this.root = root;
        this.eofToken = eofToken;
        this.diagnostics = Collections.unmodifiableList(diagnostics);
    }

    public static SyntaxTree parse(String text) {
        return new Parser(text).parse();
    }

}
