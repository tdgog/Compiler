package io.github.tdgog.compiler.treeparser.syntax.statements;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxNode;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
public class BlockStatementSyntax extends StatementSyntax {

    private final SyntaxToken openBraceToken;
    private final ArrayList<StatementSyntax> statements;
    private final SyntaxToken closeBraceToken;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.BlockStatement;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return null;
    }

}
