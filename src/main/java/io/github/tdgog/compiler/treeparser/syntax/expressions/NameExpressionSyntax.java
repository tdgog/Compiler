package io.github.tdgog.compiler.treeparser.syntax.expressions;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class NameExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken identifierToken;


    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.NameExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Collections.singletonList(identifierToken));
    }
}
