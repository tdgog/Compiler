package io.github.tdgog.compiler.TreeParser.Syntax.Expressions;

import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxNode;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxToken;
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
