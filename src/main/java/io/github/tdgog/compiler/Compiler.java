package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Binder.Binder;
import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.CodeAnalysis.Diagnostic;
import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.CodeAnalysis.TextSpan;
import io.github.tdgog.compiler.Evaluation.Evaluator;
import io.github.tdgog.compiler.Logging.Colors;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxNode;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxToken;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxTree;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

/**
 * The main class of the compiler
 */
public class Compiler {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean showTree = false;

        while (true) {
            // Get the line from the shell
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null || line.isBlank() || line.isEmpty())
                return;

            if (line.equalsIgnoreCase("!showtree")) {
                showTree = !showTree;
                System.out.println("Show parse tree: " + showTree);
                continue;
            } else if (line.equalsIgnoreCase("!clear")) {
                System.out.println("\n".repeat(100));
                continue;
            }

            // Parse the line into a bound syntax tree
            SyntaxTree syntaxTree = SyntaxTree.parse(line);
            Binder binder = new Binder();
            BoundExpression boundExpression = binder.bindExpression(syntaxTree.getRoot());

            // Get the diagnostics from the parser & binder
            // Freeze the diagnostic collection to prevent any more diagnostics being added
            DiagnosticCollection diagnostics = DiagnosticCollection.createFrozen(
                    syntaxTree.getDiagnostics(),
                    binder.getDiagnostics());

            // Display the syntax tree
            if (showTree)
                prettyPrint(syntaxTree.getRoot());

            // If any errors were found, display them
            // Otherwise, execute the line
            if (!diagnostics.isEmpty()) {
                for (Diagnostic diagnostic : diagnostics) {
                    TextSpan span = diagnostic.textSpan();
                    System.out.println(Colors.Foreground.RED + diagnostic + Colors.RESET);
                    System.out.println("\t" + line);
                    System.out.println("\t"
                            + Colors.Foreground.RED
                            + " ".repeat(span.start())
                            + "^".repeat(span.length())
                            + Colors.RESET);
                }
            } else {
                // Evaluate the syntax tree
                Evaluator evaluator = new Evaluator(boundExpression);
                Object result = evaluator.evaluate();
                System.out.println(result);
            }
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
            lastChild = node.getChildren().getLast();

        // Recursively print the children of this node
        indent += isFirst ? "" : isLast ? "    " : "│   ";
        for (SyntaxNode child : node.getChildren())
            prettyPrint(child, indent, false, child == lastChild);
    }

    // Disable org.reflections logging
    static {
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");
    }

}
