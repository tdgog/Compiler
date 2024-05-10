package io.github.tdgog.compiler;

import io.github.tdgog.compiler.binder.Binder;
import io.github.tdgog.compiler.binder.scope.BoundGlobalScope;
import io.github.tdgog.compiler.codeanalysis.logging.Colors;
import io.github.tdgog.compiler.codeanalysis.VariableCollection;
import io.github.tdgog.compiler.evaluation.EvaluationResult;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxTree;
import lombok.Getter;

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
    @Getter
    private static final Writer writer = new PrintWriter(System.out);
    private static final VariableCollection variables = new VariableCollection();
    private static Compilation previous;

    public static void main(String[] args) throws IOException {
        boolean showTree = false;

        String lineBuffer = "";
        while (true) {
            // Get the line from the shell - a line ends when a semicolon is found.
            // Anything remaining after the semicolon is stored in a buffer to be used for the next line.
            System.out.print(Colors.Foreground.GREEN + "» " + Colors.RESET);
            StringJoiner lineBuilder = new StringJoiner("\n", lineBuffer, "");
            while (!lineBuilder.toString().contains(";")) {
                if (lineBuilder.length() != 0)
                    System.out.print(Colors.Foreground.GREEN + "› " + Colors.RESET);
                lineBuilder.add(scanner.nextLine());
            }
            String line = lineBuilder.toString();
            lineBuffer = line.substring(line.indexOf(';') + 1);
            line = line.substring(0, line.indexOf(';'));

            // Debug functionality
            if (line.equalsIgnoreCase("#showtree")) {
                showTree = !showTree;
                System.out.println("Show parse tree: " + showTree);
                continue;
            } else if (line.equalsIgnoreCase("#clear")) {
                System.out.println("\n".repeat(100));
                continue;
            } else if (line.equalsIgnoreCase("#quit")) {
                break;
            } else if (line.equalsIgnoreCase("#variables")) {
                System.out.println(variables);
                continue;
            } else if (line.equalsIgnoreCase("#reset")) {
                previous = null;
                continue;
            }

            // Parse the line into a bound syntax tree
            SyntaxTree syntaxTree = SyntaxTree.parse(line);

            // Compile and get result
            Compilation compilation = previous == null
                    ? new Compilation(syntaxTree)
                    : previous.continueWith(syntaxTree);
            EvaluationResult result = compilation.evaluate(line, variables);

            // Display the syntax tree
            if (showTree) {
                syntaxTree.getRoot().writeTo(writer);
                writer.flush();
            }

            if (result.diagnostics().isEmpty()) {
                System.out.println("  " + Colors.Foreground.YELLOW + result.value() + Colors.RESET);
                previous = compilation;
            }
        }
    }

    private BoundGlobalScope globalScope;
    private synchronized BoundGlobalScope getGlobalScope(SyntaxTree syntaxTree) {
        if (globalScope == null)
            globalScope = Binder.bindGlobalScope(syntaxTree);
        return globalScope;
    }

    // Disable org.reflections logging
    static {
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");
    }

}
