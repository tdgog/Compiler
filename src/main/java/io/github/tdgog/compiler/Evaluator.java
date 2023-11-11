package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Binder.*;
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
            BoundUnaryOperatorKind syntaxKind = expression.getOperatorKind();

            if (syntaxKind == BoundUnaryOperatorKind.Identity)
                return operand;
            else if (syntaxKind == BoundUnaryOperatorKind.Negation) {
                if (operand instanceof Double o)
                    return -o;
                if (operand instanceof Integer o)
                    return -o;
            }

            throw new RuntimeException("Unexpected unary operator " + syntaxKind);
        }
        else if (root instanceof BoundBinaryExpression expression) {
            Number leftExpression = (Number) evaluateExpression(expression.getLeft());
            Number rightExpression = (Number) evaluateExpression(expression.getRight());

            if (leftExpression instanceof Double || rightExpression instanceof Double) {
                double left = leftExpression.doubleValue();
                double right = rightExpression.doubleValue();

                return switch (expression.getOperatorKind()) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    default -> throw new RuntimeException("Unexpected binary operator " + expression.getOperatorKind());
                };
            }

            if (leftExpression instanceof Integer || rightExpression instanceof Integer) {
                int left = leftExpression.intValue();
                int right = rightExpression.intValue();

                return switch (expression.getOperatorKind()) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    case Modulo -> left % right;
                };
            }

        }

        throw new RuntimeException("Unexpected node " + root.getBoundNodeKind());
    }

}
