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

    public BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind operatorKind, Object operandType, Object resultType) {
        this(syntaxKind, operatorKind, operandType, operandType, resultType);
    }

    private static final BoundBinaryOperator[] operators = {
            // Integer only
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Integer.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Integer.class),
            new BoundBinaryOperator(SyntaxKind.ModuloToken, BoundBinaryOperatorKind.Modulo, Integer.class),
            new BoundBinaryOperator(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals, Integer.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Integer.class, Boolean.class),

            // Double only
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Double.class),
            new BoundBinaryOperator(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals, Double.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Double.class, Boolean.class),

            // Integer +-/* Double
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Integer.class, Double.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals, Integer.class, Double.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Integer.class, Double.class, Boolean.class),

            // Double +-/* Integer
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division, Double.class, Integer.class, Double.class),
            new BoundBinaryOperator(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals, Double.class, Integer.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Double.class, Integer.class, Boolean.class),

            // Boolean
            new BoundBinaryOperator(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Boolean.class),
    };

    public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Object leftType, Object rightType) {
        for (BoundBinaryOperator operator : operators)
            if (operator.syntaxKind == syntaxKind && operator.leftType == leftType && operator.rightType == rightType)
                return operator;
        return null;
    }

}
