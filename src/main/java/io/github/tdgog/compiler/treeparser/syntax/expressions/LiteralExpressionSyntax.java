package io.github.tdgog.compiler.treeparser.syntax.expressions;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a single literal value.
 */
@Getter
@AllArgsConstructor
public final class LiteralExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken token;
    private final Object value;

    public LiteralExpressionSyntax(SyntaxToken token) {
        this.token = token;
        this.value = token.getValue();
    }

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.LiteralExpression;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Collections.singletonList(token));
    }

}
