package io.github.tdgog.compiler.Syntax.Expressions;

import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxNode;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents an expression surrounded by brackets, e.g. (1 + 2)
 */
@AllArgsConstructor
public class BracketExpressionSyntax extends ExpressionSyntax {

    SyntaxToken openBracketToken;
    @Getter
    ExpressionSyntax expression;
    SyntaxToken closeBracketToken;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.BracketExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(openBracketToken, expression, closeBracketToken));
    }
}
