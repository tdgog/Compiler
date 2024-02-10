package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Binder.Binder;
import io.github.tdgog.compiler.Binder.BoundExpression;
import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.CodeAnalysis.VariableCollection;
import io.github.tdgog.compiler.Evaluation.Evaluator;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxTree;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * The main class of the compiler
 */
public class Compiler {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Writer writer = new PrintWriter(System.out);
    private static final VariableCollection variables = new VariableCollection();

    public static void main(String[] args) throws IOException {
        boolean showTree = false;

        String lineBuffer = "";
        while (true) {
            // Get the line from the shell - a line ends when a semicolon is found.
            // Anything remaining after the semicolon is stored in a buffer to be used for the next line.
            System.out.print("> ");
            StringJoiner lineBuilder = new StringJoiner("\n", lineBuffer, "");
            while (!lineBuilder.toString().contains(";"))
                lineBuilder.add(scanner.nextLine());
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
            if (showTree) {
                syntaxTree.getRoot().writeTo(writer);
                writer.flush();
            }

            // Print any errors
            DiagnosticCollection diagnostics = DiagnosticCollection.createFrozen(
                    syntaxTree.getDiagnostics(),
                    binder.getDiagnostics());
            diagnostics.setSource(syntaxTree.getText());
            diagnostics.print(line);

            // Evaluate the syntax tree
            if (diagnostics.isEmpty()) {
                Evaluator evaluator = new Evaluator(boundExpression, variables);
                Object result = evaluator.evaluate();
                System.out.println(result);
            }
        }
    }

    // Disable org.reflections logging
    static {
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");
    }

}
