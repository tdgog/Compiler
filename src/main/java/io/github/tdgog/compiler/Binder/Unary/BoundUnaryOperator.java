package io.github.tdgog.compiler.Binder.Unary;

import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoundUnaryOperator {

    private final SyntaxKind syntaxKind;
    private final BoundUnaryOperatorKind operatorKind;
    private final Object operandType;
    private final Object resultType;

    public BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind operatorKind, Object operandType) {
        this(syntaxKind, operatorKind, operandType, operandType);
    }

    private static final BoundUnaryOperator[] operators = {
            new BoundUnaryOperator(SyntaxKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, Boolean.class),

            new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, Integer.class),
            new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, Integer.class),

            new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, Double.class),
            new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, Double.class),
    };

    public static BoundUnaryOperator bind(SyntaxKind syntaxKind, Object operandType) {
        for (BoundUnaryOperator operator : operators)
            if (operator.syntaxKind == syntaxKind && operator.operandType == operandType)
                return operator;
        return null;
    }

}
