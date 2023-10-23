package io.github.tdgog.compiler;

import io.github.tdgog.compiler.Syntax.Expressions.*;
import lombok.AllArgsConstructor;

/**
 * Evaluates numeric expressions
 */
@AllArgsConstructor
public class Evaluator {

    private final ExpressionSyntax root;

    public Number evaluate() {
        return evaluateExpression(root);
    }

    /**
     * Evaluates an expression
     * @param root The parent node of the expression
     * @return The resulting value
     */
    private Number evaluateExpression(ExpressionSyntax root) {
        if (root instanceof IntegerExpressionSyntax expression)
            return (Number) expression.getToken().getValue();
        if (root instanceof FloatExpressionSyntax expression)
            return (Number) expression.getToken().getValue();
        else if (root instanceof BinaryExpressionSyntax expression) {
            Number leftExpression = evaluateExpression(expression.getLeft());
            Number rightExpression = evaluateExpression(expression.getRight());

            if (leftExpression instanceof Double || rightExpression instanceof Double) {
                double left = leftExpression.doubleValue();
                double right = rightExpression.doubleValue();

                return switch (expression.getOperatorToken().getSyntaxKind()) {
                    case PlusToken -> left + right;
                    case MinusToken -> left - right;
                    case MultiplyToken -> left * right;
                    case DivideToken -> left / right;
                    default ->
                            throw new RuntimeException("Unexpected binary operator " + expression.getOperatorToken().getSyntaxKind());
                };
            }

            if (leftExpression instanceof Integer || rightExpression instanceof Integer) {
                int left = leftExpression.intValue();
                int right = rightExpression.intValue();

                return switch (expression.getOperatorToken().getSyntaxKind()) {
                    case PlusToken -> left + right;
                    case MinusToken -> left - right;
                    case MultiplyToken -> left * right;
                    case DivideToken -> left / right;
                    case ModuloToken -> left % right;
                    default ->
                            throw new RuntimeException("Unexpected binary operator " + expression.getOperatorToken().getSyntaxKind());
                };
            }

        } else if (root instanceof BracketExpressionSyntax expression)
            return evaluateExpression(expression.getExpression());

        throw new RuntimeException("Unexpected node " + root.getSyntaxKind());
    }

}
