package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Binder.Binder;
import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.CodeAnalysis.VariableCollection;
import io.github.tdgog.compiler.CodeAnalysis.VariableSymbol;
import io.github.tdgog.compiler.Evaluation.Evaluator;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxNode;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxToken;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxTree;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Scanner;

/**
 * The main class of the compiler
 */
public class Compiler {

    private static final Scanner scanner = new Scanner(System.in);
    private static final VariableCollection variables = new VariableCollection();

    public static void main(String[] args) {
        boolean showTree = false;

        String lineBuffer = "";
        while (true) {
            // Get the line from the shell - a line ends when a semicolon is found.
            // Anything remaining after the semicolon is stored in a buffer to be used for the next line.
            System.out.print("> ");
            StringBuilder lineBuilder = new StringBuilder(lineBuffer);
            while (!lineBuilder.toString().contains(";"))
                lineBuilder.append(scanner.nextLine());
            String line = lineBuilder.toString();
            lineBuffer = line.substring(line.indexOf(';') + 1);
            line = line.substring(0, line.indexOf(';'));

            // Debug functionality
            if (line.equalsIgnoreCase("!showtree")) {
                showTree = !showTree;
                System.out.println("Show parse tree: " + showTree);
                continue;
            } else if (line.equalsIgnoreCase("!clear")) {
                System.out.println("\n".repeat(100));
                continue;
            } else if (line.equalsIgnoreCase("!quit")) {
                break;
            } else if (line.equalsIgnoreCase("!variables")) {
                System.out.println(variables);
                continue;
            }

            // Parse the line into a bound syntax tree
            SyntaxTree syntaxTree = SyntaxTree.parse(line);
            Binder binder = new Binder(variables);
            BoundExpression boundExpression = binder.bindExpression(syntaxTree.getRoot());

            // Display the syntax tree
            if (showTree)
                printSyntaxTree(syntaxTree.getRoot());

            // Print any errors
            DiagnosticCollection diagnostics = DiagnosticCollection.createFrozen(
                    syntaxTree.getDiagnostics(),
                    binder.getDiagnostics());
            diagnostics.print(line);

            // Evaluate the syntax tree
            if (diagnostics.isEmpty()) {
                Evaluator evaluator = new Evaluator(boundExpression, variables);
                Object result = evaluator.evaluate();
                System.out.println(result);
            }
        }
    }

    /**
     * Prints the syntax tree
     * @param node The parent node to start from
     */
    private static void printSyntaxTree(SyntaxNode node) {
        printSyntaxTree(node, "", true, false);
    }

    /**
     * Prints the syntax tree
     * @param node The node to start from
     * @param indent The current indentation
     * @param isFirst Is this the first node in the entire tree?
     * @param isLast Is this the last child node of the parent?
     */
    private static void printSyntaxTree(@NotNull SyntaxNode node, String indent, boolean isFirst, boolean isLast) {
        // Print the node type and value
        String marker = isFirst ? "" : isLast ? "└───" : "├───";
        System.out.print(indent + marker + node.getSyntaxKind());
        if (node instanceof SyntaxToken token && token.getValue() != null)
            System.out.print(" " + token.getValue());
        System.out.println();

        // Find the last child of this parent node to determine which line type to use
        SyntaxNode lastChild = null;
        if (!node.getChildren().isEmpty())
            lastChild = node.getChildren().getLast();

        // Recursively print the children of this node
        indent += isFirst ? "" : isLast ? "    " : "│   ";
        for (SyntaxNode child : node.getChildren())
            printSyntaxTree(child, indent, false, child == lastChild);
    }

    // Disable org.reflections logging
    static {
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");
    }

}
