package io.github.tdgog.compiler.Binder;

import io.github.tdgog.compiler.Binder.Binary.BoundBinaryExpression;
import io.github.tdgog.compiler.Binder.Binary.BoundBinaryOperator;
import io.github.tdgog.compiler.Binder.Literal.BoundLiteralExpression;
import io.github.tdgog.compiler.Binder.Named.BoundAssignmentExpression;
import io.github.tdgog.compiler.Binder.Named.BoundVariableExpression;
import io.github.tdgog.compiler.Binder.Unary.BoundUnaryExpression;
import io.github.tdgog.compiler.Binder.Unary.BoundUnaryOperator;
import io.github.tdgog.compiler.CodeAnalysis.DiagnosticCollection;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Objects;

/**
 * Used to walk the syntax tree and aid in type checking
 */
@Getter
@RequiredArgsConstructor
public final class Binder {

    private final DiagnosticCollection diagnostics = new DiagnosticCollection();
    private final HashMap<String, Object> variables;

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        return switch (syntax.getSyntaxKind()) {
            case LiteralExpression -> bindLiteralExpression((LiteralExpressionSyntax) syntax);
            case BinaryExpression -> bindBinaryExpression((BinaryExpressionSyntax) syntax);
            case UnaryExpression -> bindUnaryExpression((UnaryExpressionSyntax) syntax);
            case BracketExpression -> bindBracketExpression((BracketExpressionSyntax) syntax);
            case NameExpression -> bindNameExpression((NameExpressionSyntax) syntax);
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
        if (!variables.containsKey(name)) {
            diagnostics.reportUndefinedName(syntax.getIdentifierToken().getTextSpan(), name);
            return new BoundLiteralExpression(0);
        }

        Object type = variables.get(name);
        return new BoundVariableExpression(name, type.getClass());
    }

    private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        String name = syntax.getIdentifierToken().getText();
        BoundExpression boundExpression = bindExpression(syntax.getExpression());
        return new BoundAssignmentExpression(name, boundExpression);
    }

}
