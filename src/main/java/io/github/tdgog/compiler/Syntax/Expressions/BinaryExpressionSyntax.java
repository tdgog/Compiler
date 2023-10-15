package io.github.tdgog.compiler.Syntax.Expressions;

import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxNode;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a binary expression, e.g. 1 + 2
 */
@AllArgsConstructor
@Getter
public class BinaryExpressionSyntax extends ExpressionSyntax {

    private final ExpressionSyntax left;
    private final SyntaxToken operatorToken;
    private final ExpressionSyntax right;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.BinaryExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(left, operatorToken, right));
    }
}
