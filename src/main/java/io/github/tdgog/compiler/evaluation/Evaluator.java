package io.github.tdgog.compiler.evaluation;

import io.github.tdgog.compiler.binder.*;
import io.github.tdgog.compiler.binder.binary.BoundBinaryExpression;
import io.github.tdgog.compiler.binder.binary.BoundBinaryOperatorKind;
import io.github.tdgog.compiler.binder.literal.BoundLiteralExpression;
import io.github.tdgog.compiler.binder.named.BoundAssignmentExpression;
import io.github.tdgog.compiler.binder.named.BoundVariableExpression;
import io.github.tdgog.compiler.binder.unary.BoundUnaryExpression;
import io.github.tdgog.compiler.binder.unary.BoundUnaryOperatorKind;
import io.github.tdgog.compiler.codeanalysis.VariableCollection;
import io.github.tdgog.compiler.codeanalysis.VariableSymbol;
import io.github.tdgog.compiler.evaluation.visitors.Visitor;
import io.github.tdgog.compiler.exceptions.UnexpectedBinaryOperatorException;
import lombok.AllArgsConstructor;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Evaluates numeric expressions
 */
@AllArgsConstructor
public final class Evaluator {

    private final BoundExpression root;
    private final VariableCollection variables;

    public Object evaluate() {
        return evaluateExpression(root);
    }

    /**
     * Evaluates an expression
     * @param node The parent node of the expression
     * @return The resulting value
     */
    private Object evaluateExpression(BoundExpression node) {
        return switch (node.getBoundNodeKind()) {
            case LiteralExpression -> evaluateLiteralExpression((BoundLiteralExpression) node);
            case VariableExpression -> evaluateVariableExpression((BoundVariableExpression) node);
            case AssignmentExpression -> evaluateAssignmentExpression((BoundAssignmentExpression) node);
            case UnaryExpression -> evaluateUnaryExpression((BoundUnaryExpression) node);
            case BinaryExpression -> evaluateBinaryExpression((BoundBinaryExpression) node);
            default -> throw new RuntimeException("Unexpected node " + node.getBoundNodeKind());
        };
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression expression) {
        Object leftExpression = evaluateExpression(expression.getLeft());
        Object rightExpression = evaluateExpression(expression.getRight());
        BoundBinaryOperatorKind operatorKind = expression.getOperator().getOperatorKind();

        if (operatorKind == BoundBinaryOperatorKind.Equals) {
            return leftExpression.equals(rightExpression);
        } else if (operatorKind == BoundBinaryOperatorKind.NotEquals) {
            return !leftExpression.equals(rightExpression);
        }

        try {
            Set<Class<? extends Visitor>> visitors = new Reflections("io.github.tdgog.compiler.evaluation.visitors").getSubTypesOf(Visitor.class);
            for (Class<? extends Visitor> clazz : visitors) {
                Visitor visitor = clazz.getDeclaredConstructor().newInstance();
                if (!visitor.acceptsOperator(operatorKind))
                    continue;

                return visitor.visit(leftExpression, rightExpression);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | UnexpectedBinaryOperatorException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Unexpected binary operator " + operatorKind);
    }

    private String tempformatter(Object o) {
        return o + "[" + o.getClass() + "]";
    }

    private Object evaluateUnaryExpression(BoundUnaryExpression expression) {
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

    private Object evaluateAssignmentExpression(BoundAssignmentExpression expression) {
        Object value = evaluateExpression(expression.getExpression());
        variables.put(new VariableSymbol(expression.getName(), expression.getType()), value);
        return value;
    }

    private Object evaluateVariableExpression(BoundVariableExpression expression) {
        return variables.get(expression.getVariable());
    }

    private Object evaluateLiteralExpression(BoundLiteralExpression expression) {
        return expression.getValue();
    }

}
