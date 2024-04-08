package io.github.tdgog.compiler.binder;

import io.github.tdgog.compiler.binder.binary.BoundBinaryExpression;
import io.github.tdgog.compiler.binder.binary.BoundBinaryOperator;
import io.github.tdgog.compiler.binder.literal.BoundLiteralExpression;
import io.github.tdgog.compiler.binder.named.BoundAssignmentExpression;
import io.github.tdgog.compiler.binder.named.BoundVariableExpression;
import io.github.tdgog.compiler.binder.scope.BoundGlobalScope;
import io.github.tdgog.compiler.binder.scope.BoundScope;
import io.github.tdgog.compiler.binder.unary.BoundUnaryExpression;
import io.github.tdgog.compiler.binder.unary.BoundUnaryOperator;
import io.github.tdgog.compiler.codeanalysis.DiagnosticCollection;
import io.github.tdgog.compiler.codeanalysis.VariableSymbol;
import io.github.tdgog.compiler.text.SourceText;
import io.github.tdgog.compiler.treeparser.syntax.expressions.*;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxTree;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Used to walk the syntax tree and aid in type checking
 */
@Getter
public final class Binder {

    private final DiagnosticCollection diagnostics = new DiagnosticCollection();
    private final BoundScope scope;

    public static BoundGlobalScope bindGlobalScope(SyntaxTree syntaxTree) {
        Binder binder = new Binder(null, syntaxTree.getText());
        BoundExpression expression = binder.bindExpression(syntaxTree.getRoot().getExpression());
        List<VariableSymbol> variables = binder.scope.getDeclaredVariables();
        DiagnosticCollection diagnostics = binder.getDiagnostics().freeze();
        return new BoundGlobalScope(null, diagnostics, variables, expression);
    }

    public Binder(BoundScope parent, SourceText source) {
        scope = new BoundScope(parent);
        diagnostics.setSource(source);
    }

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        return switch (syntax.getSyntaxKind()) {
            case LiteralExpression -> bindLiteralExpression((LiteralExpressionSyntax) syntax);
            case BinaryExpression -> bindBinaryExpression((BinaryExpressionSyntax) syntax);
            case UnaryExpression -> bindUnaryExpression((UnaryExpressionSyntax) syntax);
            case BracketExpression -> bindBracketExpression((BracketExpressionSyntax) syntax);
            case NameExpression -> bindNameExpression((NameExpressionSyntax) syntax);
            case DefinitionExpression -> bindDefinitionExpression((DefinitionExpressionSyntax) syntax);
            case AssignmentExpression -> bindAssignmentExpression((AssignmentExpressionSyntax) syntax);
            default -> throw new RuntimeException("Unexpected syntax " + syntax.getSyntaxKind());
        };
    }

    private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax) {
        return new BoundLiteralExpression(Objects.requireNonNullElse(syntax.getValue(), 0));
    }

    private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
        BoundExpression left = bindExpression(syntax.getLeft());
        BoundExpression right = bindExpression(syntax.getRight());
        BoundBinaryOperator operator = BoundBinaryOperator.bind(syntax.getOperatorToken().getSyntaxKind(), left.getType(), right.getType());

        if (operator == null) {
            diagnostics.reportUndefinedBinaryOperator(syntax.getOperatorToken(), left.getType(), right.getType());
            return left;
        }

        return new BoundBinaryExpression(left, operator, right);
    }

    private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax) {
        BoundExpression operand = bindExpression(syntax.getOperand());
        BoundUnaryOperator operator = BoundUnaryOperator.bind(syntax.getOperatorToken().getSyntaxKind(), operand.getType());

        if (operator == null) {
            diagnostics.reportUndefinedUnaryOperator(syntax.getOperatorToken(), operand.getType());
            return operand;
        }

        return new BoundUnaryExpression(operator, operand);
    }

    private BoundExpression bindBracketExpression(BracketExpressionSyntax syntax) {
        return bindExpression(syntax.getExpression());
    }

    private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
        String name = syntax.getIdentifierToken().getText();
        VariableSymbol variable = scope.tryLookup(name);

        if (variable == null) {
            diagnostics.reportUndefinedName(syntax.getIdentifierToken().getTextSpan(), name);
            return new BoundLiteralExpression(0);
        }

        return new BoundVariableExpression(variable);
    }

    private BoundExpression bindDefinitionExpression(DefinitionExpressionSyntax syntax) {
        String name = syntax.getIdentifierToken().getText();
        BoundExpression boundExpression = bindExpression(syntax.getExpression());

        VariableSymbol variable = new VariableSymbol(name, boundExpression.getType());
        if (!scope.tryDeclare(variable)) {
            diagnostics.reportVariableRedeclaration(syntax.getIdentifierToken());
            return new BoundLiteralExpression(0);
        }

        Class<?> definedDatatype = nameToDatatype(syntax.getType().getText());
        if (definedDatatype == null) {
            diagnostics.reportInvalidType(syntax.getType());
            return new BoundLiteralExpression(0);
        }
        if (definedDatatype != boundExpression.getType()) {
            diagnostics.reportIncorrectTypeAssignment(syntax.getIdentifierToken(), definedDatatype, boundExpression.getType());
            return new BoundLiteralExpression(0);
        }

        return new BoundAssignmentExpression(name, boundExpression);
    }

    private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        String name = syntax.getIdentifierToken().getText();
        BoundExpression boundExpression = bindExpression(syntax.getExpression());

        VariableSymbol variable = new VariableSymbol(name, boundExpression.getType());
        if (!scope.getDeclaredVariables().contains(variable)) {
            diagnostics.reportAttemptToAssignToUndeclaredVariable(syntax.getIdentifierToken(), syntax.getTextSpan());
            return new BoundLiteralExpression(0);
        }

        if (variable.type() != boundExpression.getType()) {
            diagnostics.reportIncorrectTypeAssignment(syntax.getIdentifierToken(), variable.type(), boundExpression.getType());
            return new BoundLiteralExpression(0);
        }

        return new BoundAssignmentExpression(name, boundExpression);
    }

    // Todo: Find a better way to do this than to have them all hardcoded in the binder
    private Class<?> nameToDatatype(String name) {
        return switch (name) {
            case "int" -> Integer.class;
            case "double" -> Double.class;
            case "boolean" -> Boolean.class;
            default -> null;
        };
    }

}
