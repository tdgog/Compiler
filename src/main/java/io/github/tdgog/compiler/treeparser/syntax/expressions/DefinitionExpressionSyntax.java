package io.github.tdgog.compiler.treeparser.syntax.expressions;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public class DefinitionExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken type;
    private final SyntaxToken identifierToken;
    private final SyntaxToken equalsToken;
    private final ExpressionSyntax expression;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.DefinitionExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(identifierToken, equalsToken, expression));
    }
}
