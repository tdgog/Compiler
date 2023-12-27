package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Binder.*;
import io.github.tdgog.compiler.Binder.Binary.BoundBinaryExpression;
import io.github.tdgog.compiler.Binder.Binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.Binder.Literal.BoundLiteralExpression;
import io.github.tdgog.compiler.Binder.Unary.BoundUnaryExpression;
import io.github.tdgog.compiler.Binder.Unary.BoundUnaryOperatorKind;
import lombok.AllArgsConstructor;

/**
 * Evaluates numeric expressions
 */
@AllArgsConstructor
public final class Evaluator {

    private final BoundExpression root;

    public Object evaluate() {
        return evaluateExpression(root);
    }

    /**
     * Evaluates an expression
     * @param root The parent node of the expression
     * @return The resulting value
     */
    private Object evaluateExpression(BoundExpression root) {
        if (root instanceof BoundLiteralExpression expression)
            return expression.getValue();
        else if (root instanceof BoundUnaryExpression expression) {
            Object operand = evaluateExpression(expression.getOperand());
            BoundUnaryOperatorKind syntaxKind = expression.getOperator().getOperatorKind();

            if (syntaxKind == BoundUnaryOperatorKind.Identity)
                return operand;
            else if (syntaxKind == BoundUnaryOperatorKind.Negation) {
                if (operand instanceof Double o)
                    return -o;
                if (operand instanceof Integer o)
                    return -o;
            } else if (syntaxKind == BoundUnaryOperatorKind.LogicalNegation) {
                return !(boolean) operand;
            }

            throw new RuntimeException("Unexpected unary operator " + syntaxKind);
        }
        else if (root instanceof BoundBinaryExpression expression) {
            Object leftExpression = evaluateExpression(expression.getLeft());
            Object rightExpression = evaluateExpression(expression.getRight());
            BoundBinaryOperatorKind operatorKind = expression.getOperator().getOperatorKind();

            if (operatorKind == BoundBinaryOperatorKind.Equals) {
                return leftExpression.equals(rightExpression);
            } else if (operatorKind == BoundBinaryOperatorKind.NotEquals) {
                return !leftExpression.equals(rightExpression);
            }

            if (leftExpression instanceof Double || rightExpression instanceof Double) {
                double left = (double) leftExpression;
                double right = (double) rightExpression;

                return switch (operatorKind) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    case LogicalAnd -> toBoolean(left) && toBoolean(right);
                    case LogicalOr -> toBoolean(left) || toBoolean(right);
                    default -> throw new RuntimeException("Unexpected binary operator " + operatorKind);
                };
            }

            if (leftExpression instanceof Integer || rightExpression instanceof Integer) {
                int left = (int) leftExpression;
                int right = (int) rightExpression;

                return switch (operatorKind) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    case Modulo -> left % right;
                    case LogicalAnd -> toBoolean(left) && toBoolean(right);
                    case LogicalOr -> toBoolean(left) || toBoolean(right);
                    default -> throw new RuntimeException("Unexpected binary operator " + operatorKind);
                };
            }

            if (leftExpression instanceof Boolean left && rightExpression instanceof Boolean right) {
                return switch (operatorKind) {
                    case LogicalAnd -> left && right;
                    case LogicalOr -> left || right;
                    default -> throw new RuntimeException("Unexpected binary operator " + operatorKind);
                };
            }

        }

        throw new RuntimeException("Unexpected node " + root.getBoundNodeKind());
    }

    private boolean toBoolean(int i) {
        return i != 0;
    }

    private boolean toBoolean(double d) {
        return d != 0;
    }

}
