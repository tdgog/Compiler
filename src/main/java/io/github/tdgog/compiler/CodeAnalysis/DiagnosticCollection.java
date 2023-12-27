package io.github.tdgog.compiler.CodeAnalysis;

import io.github.tdgog.compiler.Logging.ClassNameConverter;
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

    public void addAll(DiagnosticCollection diagnostics) {
        if (frozen)
            return;

        this.diagnostics.addAll(diagnostics.diagnostics);
    }

    public boolean isEmpty() {
        return diagnostics.isEmpty();
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
