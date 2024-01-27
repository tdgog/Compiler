package io.github.tdgog.compiler.TreeParser.Syntax;

import io.github.tdgog.compiler.CodeAnalysis.TextSpan;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * The base class for all nodes on an abstract syntax tree
 */
public abstract class SyntaxNode {

    public abstract SyntaxKind getSyntaxKind();

    public abstract ArrayList<SyntaxNode> getChildren();

    public TextSpan getTextSpan() {
        ArrayList<SyntaxNode> children = getChildren();
        return TextSpan.fromBounds(children.getFirst().getTextSpan().start(), children.getLast().getTextSpan().end());
    }

    public void writeTo(Writer writer) throws IOException {
        printSyntaxTree(writer, this);
    }

    /**
     * Prints the syntax tree
     * @param node The parent node to start from
     */
    private static void printSyntaxTree(Writer writer, SyntaxNode node) throws IOException {
        printSyntaxTree(writer, node, "", true, false);
    }

    /**
     * Prints the syntax tree
     * @param node The node to start from
     * @param indent The current indentation
     * @param isFirst Is this the first node in the entire tree?
     * @param isLast Is this the last child node of the parent?
     */
    private static void printSyntaxTree(Writer writer, @NotNull SyntaxNode node, String indent, boolean isFirst, boolean isLast) throws IOException {
        // Print the node type and value
        String marker = isFirst ? "" : isLast ? "└───" : "├───";
        writer.write(indent + marker + node.getSyntaxKind());
        if (node instanceof SyntaxToken token && token.getValue() != null)
            writer.write(" " + token.getValue());
        writer.write('\n');

        // Find the last child of this parent node to determine which line type to use
        SyntaxNode lastChild = null;
        if (!node.getChildren().isEmpty())
            lastChild = node.getChildren().getLast();

        // Recursively print the children of this node
        indent += isFirst ? "" : isLast ? "    " : "│   ";
        for (SyntaxNode child : node.getChildren())
            printSyntaxTree(writer, child, indent, false, child == lastChild);
    }

}
