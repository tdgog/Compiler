package io.github.tdgog.compiler.TreeParser.Syntax;

import java.util.ArrayList;

/**
 * The base class for all nodes on an abstract syntax tree
 */
public abstract class SyntaxNode {

    public abstract SyntaxKind getSyntaxKind();

    public abstract ArrayList<SyntaxNode> getChildren();

}
