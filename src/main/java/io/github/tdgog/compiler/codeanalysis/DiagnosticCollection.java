package io.github.tdgog.compiler.codeanalysis;

import io.github.tdgog.compiler.codeanalysis.logging.ClassNameConverter;
import io.github.tdgog.compiler.codeanalysis.logging.Colors;
import io.github.tdgog.compiler.text.SourceText;
import io.github.tdgog.compiler.text.TextSpan;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public class DiagnosticCollection implements Iterable<Diagnostic> {

    @Setter
    private SourceText source;
    private final List<Diagnostic> diagnostics = new ArrayList<>();
    private boolean frozen = false;

    private void report(TextSpan textSpan, String message) {
        if (frozen)
            return;

        Diagnostic diagnostic = new Diagnostic(textSpan, message);
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

    public void reportAttemptToAssignToUndeclaredVariable(SyntaxToken token, TextSpan span) {
        String message = "Variable '" + token.getText() + "' has not been declared. Consider declaring the variable as " +
                "[type] " + source.substring(span) + ";";
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
        if (source == null)
            throw new RuntimeException("Diagnostic source not set. Call DiagnosticCollection#setSource(SourceText) before printing.");

        for (Diagnostic diagnostic : diagnostics) {
            int lineIndex = source.getLineIndex(diagnostic.textSpan().start());
            int lineNumber = lineIndex + 1;
            int character = diagnostic.textSpan().start() - source.getLines().get(lineIndex).start() + 1;

            TextSpan span = diagnostic.textSpan();
            System.out.println(Colors.Foreground.RED + "  Line " + lineNumber + " column " + character + ": " + diagnostic + Colors.RESET);
            for (String l : line.split("\\n"))
                System.out.println("\t" + l);
            System.out.println("\t"
                    + Colors.Foreground.RED
                    + " ".repeat(character - 1)
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

    public DiagnosticCollection freeze() {
        return createFrozen(this);
    }

    @NotNull
    @Override
    public Iterator<Diagnostic> iterator() {
        return diagnostics.iterator();
    }

}
