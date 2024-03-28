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
public class UnaryExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken operatorToken;
    private final ExpressionSyntax operand;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.UnaryExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(operatorToken, operand));
    }
}