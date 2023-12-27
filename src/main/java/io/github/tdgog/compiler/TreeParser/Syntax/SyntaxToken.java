package io.github.tdgog.compiler.TreeParser.Syntax;

import io.github.tdgog.compiler.CodeAnalysis.TextSpan;
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

    /**
     * Alternative constructor with the default value of null
     * @param syntaxKind The type of token
     * @param position The index of the token
     * @param text The content of the token
     */
    public SyntaxToken(SyntaxKind syntaxKind, int position, String text) {
        this(syntaxKind, position, text, null);
    }

    public TextSpan getTextSpan() {
        return new TextSpan(position, text.length());
    }

    @Override
    public ArrayList<SyntaxNode> getChildren() {
        return new ArrayList<>();
    }

}
