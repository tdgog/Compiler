package io.github.tdgog.compiler.Syntax;

import io.github.tdgog.compiler.Parser;
import io.github.tdgog.compiler.Syntax.Expressions.ExpressionSyntax;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Represents a tree of syntax tokens
 */
@Getter
public class SyntaxTree {

    private ExpressionSyntax root;
    private SyntaxToken eofToken;
    private List<String> diagnostics;

    public SyntaxTree(ExpressionSyntax root, SyntaxToken eofToken, List<String> diagnostics) {
        this.root = root;
        this.eofToken = eofToken;
        this.diagnostics = Collections.unmodifiableList(diagnostics);
    }

    public static SyntaxTree parse(String text) {
        return new Parser(text).parse();
    }

}
