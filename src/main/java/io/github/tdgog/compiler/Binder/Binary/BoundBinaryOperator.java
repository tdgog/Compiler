package io.github.tdgog.compiler.Binder.Binary;

import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoundBinaryOperator {

    private final SyntaxKind syntaxKind;
    private final BoundBinaryOperatorKind operatorKind;
    private final Object leftType;
    private final Object rightType;
    private final Object resultType;

    public BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind operatorKind, Object type) {
        this(syntaxKind, operatorKind, type, type, type);
    }

    private static final BoundBinaryOperator[] operators = {
            // Integer only
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Integer.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Integer.class),
            new BoundBinaryOperator(SyntaxKind.ModuloToken, BoundBinaryOperatorKind.Modulo, Integer.class),

            // Double only
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Double.class),

            // Integer +-/* Double
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Integer.class, Double.class, Double.class),

            // Double +-/* Integer
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Double.class, Integer.class, Double.class),

            // Boolean
            new BoundBinaryOperator(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean.class),
    };

    public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Object leftType, Object rightType) {
        for (BoundBinaryOperator operator : operators)
            if (operator.syntaxKind == syntaxKind && operator.leftType == leftType && operator.rightType == rightType)
                return operator;
        return null;
    }

}
