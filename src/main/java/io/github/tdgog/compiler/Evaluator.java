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
            } else if (syntaxKind == BoundUnaryOperatorKind.LogicalNegation) {
                return !(boolean) operand;
            }

            throw new RuntimeException("Unexpected unary operator " + syntaxKind);
        }
        else if (root instanceof BoundBinaryExpression expression) {
            Object leftExpression = evaluateExpression(expression.getLeft());
            Object rightExpression = evaluateExpression(expression.getRight());

            if (leftExpression instanceof Double || rightExpression instanceof Double) {
                double left = (double) leftExpression;
                double right = (double) rightExpression;

                return switch (expression.getOperatorKind()) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    default -> throw new RuntimeException("Unexpected binary operator " + expression.getOperatorKind());
                };
            }

            if (leftExpression instanceof Integer || rightExpression instanceof Integer) {
                int left = (int) leftExpression;
                int right = (int) rightExpression;

                return switch (expression.getOperatorKind()) {
                    case Addition -> left + right;
                    case Subtraction -> left - right;
                    case Multiplication -> left * right;
                    case Division -> left / right;
                    case Modulo -> left % right;
                    default -> throw new RuntimeException("Unexpected binary operator " + expression.getOperatorKind());
                };
            }

            if (leftExpression instanceof Boolean left && rightExpression instanceof Boolean right) {
                return switch (expression.getOperatorKind()) {
                    case LogicalAnd -> left && right;
                    case LogicalOr -> left || right;
                    default -> throw new RuntimeException("Unexpected binary operator " + expression.getOperatorKind());
                };
            }

        }

        throw new RuntimeException("Unexpected node " + root.getBoundNodeKind());
    }

}
