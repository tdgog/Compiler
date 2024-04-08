package io.github.tdgog.compiler.treeparser.syntax.expressions;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class CompilationUnitSyntax extends SyntaxNode {

    private final ExpressionSyntax expression;
    private final SyntaxToken eofToken;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.CompilationUnit;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(expression, eofToken));
    }
}
