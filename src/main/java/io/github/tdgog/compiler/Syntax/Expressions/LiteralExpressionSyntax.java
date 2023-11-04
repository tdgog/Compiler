package io.github.tdgog.compiler.Syntax.Expressions;

import io.github.tdgog.compiler.Syntax.SyntaxKind;
import io.github.tdgog.compiler.Syntax.SyntaxNode;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a single literal value.
 */
@Getter
@AllArgsConstructor
public final class LiteralExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken token;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.LiteralExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(token));
    }

}
