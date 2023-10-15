package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Syntax.Expressions.ExpressionSyntax;
import io.github.tdgog.compiler.Syntax.SyntaxNode;
import io.github.tdgog.compiler.Syntax.SyntaxToken;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

/**
 * The main class of the compiler
 */
public class Compiler {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // Get the line from the shell
            String line = scanner.nextLine();
            if(line == null || line.isBlank() || line.isEmpty())
                return;

            // Parse and print the syntax tree
            Parser parser = new Parser(line);
            ExpressionSyntax expression = parser.parse();
            prettyPrint(expression);
        }
    }

    /**
     * Prints the syntax tree
     * @param node The parent node to start from
     */
    private static void prettyPrint(SyntaxNode node) {
        prettyPrint(node, "", true, false);
    }

    /**
     * Prints the syntax tree
     * @param node The node to start from
     * @param indent The current indentation
     * @param isFirst Is this the first node in the entire tree?
     * @param isLast Is this the last child node of the parent?
     */
    private static void prettyPrint(@NotNull SyntaxNode node, String indent, boolean isFirst, boolean isLast) {
        // Print the node type and value
        String marker = isFirst ? "" : isLast ? "└───" : "├───";
        System.out.print(indent + marker + node.getSyntaxKind());
        if (node instanceof SyntaxToken token && token.getValue() != null)
            System.out.print(" " + token.getValue());
        System.out.println();

        // Find the last child of this parent node to determine which line type to use
        SyntaxNode lastChild = null;
        if (!node.getChildren().isEmpty())
            lastChild = node.getChildren().get(node.getChildren().size() - 1);

        // Recursively print the children of this node
        indent += isFirst ? "" : isLast ? "    " : "│   ";
        for (SyntaxNode child : node.getChildren())
            prettyPrint(child, indent, false, child == lastChild);
    }

}
