package io.github.tdgog.compiler.Binder.Binary;

import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

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

    private static final ArrayList<BoundBinaryOperator> operators = new ArrayList<>();

    static {
        @AllArgsConstructor
        @Getter
        final class TypeBindings {
            private final Object primaryType;
            private final Object[] secondaryTypes;
            private final HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup;

            public TypeBindings(Object primary, Object secondary, Object result, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                this(primary, new Object[]{secondary}, boundKindLookup);
            }

            public TypeBindings(Object primary, Object secondary, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                this(primary, new Object[]{secondary}, primary, boundKindLookup);
            }

            public TypeBindings(Object primary, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                this(primary, new Object[]{primary}, primary, boundKindLookup);
            }
        }

        /* PRIMARY ---------------------> [SECONDARY]
        *     |    defined operators for
        *     |                |-------------> {SyntaxKind : BoundBinaryOperatorKind}
        *     |                 in the format
        *     |---------------------------------> RESULT_TYPE || PRIMARY
        *      which have a result type equal to
        * */
        final TypeBindings[] bindings = new TypeBindings[] {
                new TypeBindings(Integer.class, new HashMap<>() {{
                    put(SyntaxKind.ModuloToken, BoundBinaryOperatorKind.Modulo);
                    put(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division);
                }}),
                new TypeBindings(Double.class, Integer.class, new HashMap<>() {{
                    put(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition);
                    put(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction);
                    put(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication);
                    put(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division);
                }}),

                /* TODO: There's lots of repetition here, find a way to update this to be more concise
                *   Perhaps consider an option for all types in a TypeBinding to be considered primary
                *   and therefore allow all permutations of typing as long as the result type is fixed */
                new TypeBindings(Boolean.class, new Object[]{Integer.class, Double.class}, new HashMap<>() {{
                    put(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr);
                    put(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd);
                    put(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals);
                    put(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals);
                }}),
                new TypeBindings(Integer.class, Double.class, Boolean.class, new HashMap<>() {{
                    put(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr);
                    put(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd);
                    put(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals);
                    put(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals);
                }}),
                new TypeBindings(Double.class, Boolean.class, Boolean.class, new HashMap<>() {{
                    put(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr);
                    put(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd);
                    put(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals);
                    put(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals);
                }}),
        };

        /* TODO: Walk through the bindings and add them to the operators list
        *   Note that type bindings are NOT defined between secondaries - for example the
        *   TypeBindings(Double.class, Integer.class, ...) will not define a binding for Integer + Integer, only
        *   Double + Integer, Double + Double, and Integer + Double */
    }

    public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Object leftType, Object rightType) {
        for (BoundBinaryOperator operator : operators)
            if (operator.syntaxKind == syntaxKind && operator.leftType == leftType && operator.rightType == rightType)
                return operator;
        return null;
    }

}
