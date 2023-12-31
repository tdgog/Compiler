package io.github.tdgog.compiler.CodeAnalysis;

import io.github.tdgog.compiler.CodeAnalysis.Logging.ClassNameConverter;
import io.github.tdgog.compiler.CodeAnalysis.Logging.Colors;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxToken;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DiagnosticCollection implements Iterable<Diagnostic> {

    private final List<Diagnostic> diagnostics = new ArrayList<>();
    private boolean frozen = false;

    private void report(TextSpan textSpan, String message) {
        if (frozen)
            return;

        Diagnostic diagnostic = new Diagnostic(textSpan, "Error: " + message);
        diagnostics.add(diagnostic);
    }

    public void reportBadCharacter(int position, char character) {
        TextSpan textSpan = new TextSpan(position, 1);
        String message = "Bad character: " + character + ".";
        report(textSpan, message);
    }

    public void reportInvalidNumber(TextSpan textSpan, String content, Class<?> type) {
        String message = content + " is not a valid " + ClassNameConverter.toFriendlyName(type) + ".";
        report(textSpan, message);
    }

    public void reportUnexpectedToken(SyntaxToken recieved, SyntaxKind expected) {
        reportUnexpectedToken(recieved, new SyntaxKind[]{expected});
    }

    public void reportUnexpectedToken(SyntaxToken recieved, SyntaxKind[] expectedPossibilities) {
        StringJoiner expectedKinds = new StringJoiner("|");
        for (SyntaxKind kind : expectedPossibilities)
            expectedKinds.add("<" + kind + ">");

        TextSpan textSpan = recieved.getTextSpan();
        String message = "Unexpected token <" + recieved.getSyntaxKind() + ">, expected " + expectedKinds + ".";
        report(textSpan, message);
    }

    public void reportUndefinedUnaryOperator(SyntaxToken operatorToken, Class<?> type) {
        TextSpan textSpan = operatorToken.getTextSpan();
        String message = "Undefined unary operator " + operatorToken.getText() + " for type " + ClassNameConverter.toFriendlyName(type) + ".";
        report(textSpan, message);
    }

    public void reportUndefinedBinaryOperator(SyntaxToken operatorToken, Class<?> leftType, Class<?> rightType) {
        TextSpan textSpan = operatorToken.getTextSpan();
        String message = "Undefined binary operator '" + operatorToken.getText() + "' for types "
                + ClassNameConverter.toFriendlyName(leftType) + " and " + ClassNameConverter.toFriendlyName(rightType) + ".";
        report(textSpan, message);
    }

    public void reportUndefinedName(TextSpan textSpan, String name) {
        String message = "Variable '" + name + "' doesn't exist.";
        report(textSpan, message);
    }

    public void reportIncorrectTypeAssignment(SyntaxToken token, Class<?> expected, Class<?> recieved) {
        String message = "Attempt to assign " + ClassNameConverter.toFriendlyName(recieved) + " to "
                + ClassNameConverter.toFriendlyName(expected) + " variable '" + token.getText() + "'.";
        report(token.getTextSpan(), message);
    }

    public void reportInvalidType(SyntaxToken type) {
        String message = "Unknown type '" + type.getText() + "'";
        report(type.getTextSpan(), message);
    }

    public void reportVariableRedeclaration(SyntaxToken token) {
        String message = "Variable '" + token.getText() + "' has already been declared. Consider reassigning it or " +
                "declaring a variable with a different name.";
        report(token.getTextSpan(), message);
    }

    public void addAll(DiagnosticCollection diagnostics) {
        if (frozen)
            return;

        this.diagnostics.addAll(diagnostics.diagnostics);
    }

    public boolean isEmpty() {
        return diagnostics.isEmpty();
    }

    public void print(String line) {
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
    }

    public static DiagnosticCollection createFrozen(DiagnosticCollection... diagnosticCollections) {
        DiagnosticCollection diagnostics = new DiagnosticCollection();
        for (DiagnosticCollection collection : diagnosticCollections)
            diagnostics.addAll(collection);
        diagnostics.frozen = true;
        return diagnostics;
    }

    @NotNull
    @Override
    public Iterator<Diagnostic> iterator() {
        return diagnostics.iterator();
    }
}
