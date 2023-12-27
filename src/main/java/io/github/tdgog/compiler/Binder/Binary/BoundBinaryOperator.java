package io.github.tdgog.compiler.Binder.Binary;

import io.github.tdgog.compiler.TreeParser.Syntax.SyntaxKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Arrays.copyOf;

@Getter
@RequiredArgsConstructor
public class BoundBinaryOperator {

    private final SyntaxKind syntaxKind;
    private final BoundBinaryOperatorKind operatorKind;
    private final Class<?> leftType;
    private final Class<?> rightType;
    private final Class<?> resultType;

    /* Define a list of all valid operations */
    private static final ArrayList<BoundBinaryOperator> operators = new ArrayList<>();
    static {
        @RequiredArgsConstructor
        @Getter
        final class TypeBindings {
            private final Class<?> primaryType;
            private final Class<?>[] secondaryTypes;
            private final Class<?> resultType;
            private final HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup;
            @Setter private boolean allPrimary = false;

            public TypeBindings(Class<?> primary, Class<?> secondary, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                this(primary, new Class<?>[]{secondary}, primary, boundKindLookup);
            }

            public TypeBindings(Class<?> primary, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                this(primary, new Class<?>[]{primary}, primary, boundKindLookup);
            }

            public static TypeBindings allUndefinedPermutations(Class<?>[] primary, Class<?> result, HashMap<SyntaxKind, BoundBinaryOperatorKind> boundKindLookup) {
                TypeBindings typeBindings = new TypeBindings(null, primary, result, boundKindLookup);
                typeBindings.setAllPrimary(true);
                return typeBindings;
            }
        }

        final TypeBindings[] bindings = new TypeBindings[] {
                new TypeBindings(Integer.class, new HashMap<>() {{
                    put(SyntaxKind.ModuloToken, BoundBinaryOperatorKind.Modulo);
                    put(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition);
                    put(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction);
                    put(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication);
                    put(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division);
                }}),
                new TypeBindings(Double.class, Integer.class, new HashMap<>() {{
                    put(SyntaxKind.ModuloToken, BoundBinaryOperatorKind.Modulo);
                    put(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition);
                    put(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction);
                    put(SyntaxKind.MultiplyToken, BoundBinaryOperatorKind.Multiplication);
                    put(SyntaxKind.DivideToken, BoundBinaryOperatorKind.Division);
                }}),
                TypeBindings.allUndefinedPermutations(new Class<?>[]{Boolean.class, Integer.class, Double.class}, Boolean.class, new HashMap<>() {{
                    put(SyntaxKind.DoublePipeToken, BoundBinaryOperatorKind.LogicalOr);
                    put(SyntaxKind.DoubleAmpersandToken, BoundBinaryOperatorKind.LogicalAnd);
                    put(SyntaxKind.DoubleEqualsToken, BoundBinaryOperatorKind.Equals);
                    put(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals);
                }}),
        };

        final class OperatorRegistrator {
            private static final ArrayList<String> registeredOperators = new ArrayList<>();

            /**
             * Registers an operator
             * @param syntaxKind The operator's SyntaxKind
             * @param leftOperand The type of the left operand
             * @param rightOperand The type of the right operand
             * @param binding The TypeBindings information
             */
            public static void register(SyntaxKind syntaxKind, Class<?> leftOperand, Class<?> rightOperand, TypeBindings binding) {
                BoundBinaryOperator operator = new BoundBinaryOperator(
                        syntaxKind,
                        binding.getBoundKindLookup().get(syntaxKind),
                        leftOperand,
                        rightOperand,
                        binding.getResultType());
                String operatorInformation = operator.getOperatorInformation();
                if (!registeredOperators.contains(operatorInformation)) {
                    operators.add(operator);
                    registeredOperators.add(operatorInformation);
                }
            }
        }

        for (TypeBindings binding : bindings) {
            if (binding.isAllPrimary()) {
                // Bind all operands with all operands across all kinds of operator
                for (Class<?> leftOperand : binding.getSecondaryTypes())
                    for (Class<?> rightOperand : binding.getSecondaryTypes())
                        for (SyntaxKind kind : binding.getBoundKindLookup().keySet())
                            OperatorRegistrator.register(kind, leftOperand, rightOperand, binding);
            } else {
                // Add the primary type into the array of secondary types
                Class<?>[] secondaryTypes = copyOf(binding.getSecondaryTypes(), binding.getSecondaryTypes().length + 1);
                secondaryTypes[secondaryTypes.length - 1] = binding.getPrimaryType();

                // Bind the primary operand with all secondary operands and itself across all kinds of operator
                for (Class<?> otherOperand : secondaryTypes) {
                    for (SyntaxKind kind : binding.getBoundKindLookup().keySet()) {
                        OperatorRegistrator.register(kind, binding.getPrimaryType(), otherOperand, binding);
                        OperatorRegistrator.register(kind, otherOperand, binding.getPrimaryType(), binding);
                    }
                }
            }
        }
    }

    public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Class<?> leftType, Class<?> rightType) {
        for (BoundBinaryOperator operator : operators)
            if (operator.syntaxKind == syntaxKind && operator.leftType == leftType && operator.rightType == rightType)
                return operator;
        return null;
    }

    private String getOperatorInformation() {
        return String.valueOf(syntaxKind) + operatorKind + leftType + rightType + resultType;
    }
}
