package io.github.tdgog.compiler.treeparser.syntax.statements;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.expressions.ExpressionSyntax;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class ExpressionStatementSyntax extends StatementSyntax {

    private final ExpressionSyntax expression;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.ExpressionStatement;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Collections.singletonList(expression));
    }

}
