package io.github.tdgog.compiler.TreeParser.Syntax;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/**
 * A token that can be represented on an abstract syntax tree
 */
@AllArgsConstructor
@Getter
public class SyntaxToken extends SyntaxNode {

    private final SyntaxKind syntaxKind;
    private final int position;
    private final String text;
    private final Object value;

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>();
    }

}
