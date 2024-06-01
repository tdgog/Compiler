package io.github.tdgog.compiler.treeparser.syntax;

import io.github.tdgog.compiler.treeparser.syntax.statements.StatementSyntax;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class CompilationUnitSyntax extends SyntaxNode {

    private final StatementSyntax statement;
    private final SyntaxToken eofToken;

    @Override
    public SyntaxKind getSyntaxKind() {
        return SyntaxKind.CompilationUnit;
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>(Arrays.asList(statement, eofToken));
    }
}
