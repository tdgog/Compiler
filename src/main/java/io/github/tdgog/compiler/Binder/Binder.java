package io.github.tdgog.compiler.Binder;

import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.BinaryExpressionSyntax;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.ExpressionSyntax;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.LiteralExpressionSyntax;
import io.github.tdgog.compiler.TreeParser.Syntax.Expressions.UnaryExpressionSyntax;
import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to walk the syntax tree and aid in type checking
 */
@Getter
public final class Binder {

    private final List<String> diagnostics = new ArrayList<>();

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        return switch (syntax.getSyntaxKind()) {
            case LiteralExpression -> bindLiteralExpression((LiteralExpressionSyntax) syntax);
            case BinaryExpression -> bindBinaryExpression((BinaryExpressionSyntax) syntax);
            case UnaryExpression -> bindUnaryExpression((UnaryExpressionSyntax) syntax);
            default -> throw new RuntimeException("Unexpected syntax " + syntax.getSyntaxKind());
        };
    }

    private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax) {
        Object value = syntax.getToken().getValue();
        int castValue = (value instanceof Integer) ? (int) value : 0;
        return new BoundLiteralExpression(castValue);
    }

    private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax) {
        BoundExpression operand = bindExpression(syntax.getOperand());
        BoundUnaryOperatorKind operatorKind = bindUnaryOperatorKind(syntax.getOperatorToken().getSyntaxKind(), operand.getType());

        if (operatorKind == null) {
            diagnostics.add("Unary operator '" + syntax.getOperatorToken().getText() + "' is not defined for type " + operand.getType() + ".");
            return operand;
        }

        return new BoundUnaryExpression(operatorKind, operand);
    }

    private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
        BoundExpression left = bindExpression(syntax.getLeft());
        BoundExpression right = bindExpression(syntax.getRight());
        BoundBinaryOperatorKind operatorKind = bindBinaryOperatorKind(syntax.getOperatorToken().getSyntaxKind(), left.getType(), right.getType());

        if (operatorKind == null) {
            diagnostics.add("Binary operator '" + syntax.getOperatorToken().getText() + "' is not defined for types " + left.getType() + " and " + right.getType() + ".");
            return left;
        }

        return new BoundBinaryExpression(left, operatorKind, right);
    }

    private BoundUnaryOperatorKind bindUnaryOperatorKind(SyntaxKind syntaxKind, Object operandType) {
        if (!(operandType instanceof Integer))
            return null;

        return switch (syntaxKind) {
            case PlusToken -> BoundUnaryOperatorKind.Identity;
            case MinusToken -> BoundUnaryOperatorKind.Negation;
            default -> throw new RuntimeException("Unexpected unary operator " + syntaxKind);
        };
    }

    private BoundBinaryOperatorKind bindBinaryOperatorKind(SyntaxKind syntaxKind, Object leftType, Object rightType) {
        if (leftType != Integer.class || rightType != Integer.class)
            return null;

        return switch (syntaxKind) {
            case PlusToken -> BoundBinaryOperatorKind.Addition;
            case MinusToken -> BoundBinaryOperatorKind.Subtraction;
            case MultiplyToken -> BoundBinaryOperatorKind.Multiplication;
            case DivideToken -> BoundBinaryOperatorKind.Division;
            case ModuloToken -> BoundBinaryOperatorKind.Modulo;
            default -> throw new RuntimeException("Unexpected binary operator " + syntaxKind);
        };
    }

}
