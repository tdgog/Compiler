package io.github.tdgog.compiler.treeparser.syntax.expressions;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents an expression surrounded by brackets, e.g. (1 + 2)
 */
@AllArgsConstructor
public class BracketExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken openBracketToken;
    @Getter
    private final ExpressionSyntax expression;
    private final SyntaxToken closeBracketToken;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.BracketExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(openBracketToken, expression, closeBracketToken));
    }
}
