package io.github.tdgog.compiler.treeparser;

import io.github.tdgog.compiler.treeparser.syntax.SyntaxKind;
import io.github.tdgog.compiler.treeparser.syntax.SyntaxToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class LexerTest {

    private static Stream<Arguments> provideTestLexSyntaxKindArgs() {
        return Stream.of(
                Arguments.of("a", SyntaxKind.IdentifierToken),
                Arguments.of("abc", SyntaxKind.IdentifierToken),
                Arguments.of(" ", SyntaxKind.WhitespaceToken),
                Arguments.of("  ", SyntaxKind.WhitespaceToken),
                Arguments.of("\t", SyntaxKind.WhitespaceToken),
                Arguments.of("\r", SyntaxKind.WhitespaceToken),
                Arguments.of("\n", SyntaxKind.WhitespaceToken),
                Arguments.of("\r\n", SyntaxKind.WhitespaceToken),
                Arguments.of("1", SyntaxKind.IntegerToken),
                Arguments.of("12", SyntaxKind.IntegerToken),
                Arguments.of(".1", SyntaxKind.FloatToken),
                Arguments.of("1.1", SyntaxKind.FloatToken),
                Arguments.of("=", SyntaxKind.EqualsToken),
                Arguments.of("(", SyntaxKind.OpenBracketToken),
                Arguments.of(")", SyntaxKind.CloseBracketToken),
                Arguments.of("+", SyntaxKind.PlusToken),
                Arguments.of("-", SyntaxKind.MinusToken),
                Arguments.of("*", SyntaxKind.MultiplyToken),
                Arguments.of("/", SyntaxKind.DivideToken),
                Arguments.of("%", SyntaxKind.ModuloToken),
                Arguments.of("!", SyntaxKind.BangToken),
                Arguments.of("&&", SyntaxKind.DoubleAmpersandToken),
                Arguments.of("||", SyntaxKind.DoublePipeToken),
                Arguments.of("==", SyntaxKind.DoubleEqualsToken),
                Arguments.of("!=", SyntaxKind.BangEqualsToken),
                Arguments.of("true", SyntaxKind.TrueKeyword),
                Arguments.of("false", SyntaxKind.FalseKeyword)
        );
    }

    /**
     * This method provides a stream of arguments for the testLexSyntaxKindPair method.
     * @return A stream of arguments for the testLexSyntaxKindPair method.
     */
    private static Stream<Arguments> provideTestLexSyntaxKindPairArgs() {
        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        provideTestLexSyntaxKindArgs().forEach(first -> {
            provideTestLexSyntaxKindArgs().forEach(second -> {
                String text;
                List<Object> kinds;
                if (first.get()[1] == SyntaxKind.WhitespaceToken && second.get()[1] == SyntaxKind.WhitespaceToken) {
                    text = (String) first.get()[0] + second.get()[0];
                    kinds = List.of(SyntaxKind.WhitespaceToken);
                } else if (first.get()[1] == SyntaxKind.WhitespaceToken || second.get()[1] == SyntaxKind.WhitespaceToken) {
                    text = (String) first.get()[0] + second.get()[0];
                    kinds = List.of(first.get()[1], second.get()[1]);
                } else {
                    SyntaxKind firstKind = (SyntaxKind) first.get()[1];
                    SyntaxKind secondKind = (SyntaxKind) second.get()[1];

                    if (requiresSeparator(firstKind, secondKind)) {
                        text = first.get()[0] + " " + second.get()[0];
                        kinds = List.of(firstKind, SyntaxKind.WhitespaceToken, secondKind);
                    } else {
                        text = (String) first.get()[0] + second.get()[0];
                        kinds = List.of(firstKind, secondKind);
                    }
                }
                argumentsBuilder.add(Arguments.of(text, kinds));
            });
        });

        return argumentsBuilder.build();
    }

    /**
     * This method tests the lexer's ability to correctly identify the syntax kind of a token.
     * @param lexString The string to be lexed.
     * @param expected The expected syntax kind of the token.
     */
    @ParameterizedTest
    @MethodSource("provideTestLexSyntaxKindArgs")
    public void testLexSyntaxKind(String lexString, SyntaxKind expected) {
        // Remove the EOF token from the end of the line
        List<SyntaxKind> recieved = getSyntaxKinds(getTokens(lexString));
        recieved = recieved.subList(0, recieved.size() - 1);

        Assertions.assertEquals(Collections.singletonList(expected), recieved);
    }

    /**
     * This method tests the lexer's ability to correctly identify the syntax kind of any possible pair of tokens
     * @param lexString The string to be lexed.
     * @param expected The expected syntax kind of the token pair.
     */
    @ParameterizedTest
    @MethodSource("provideTestLexSyntaxKindPairArgs")
    public void testLexSyntaxKindPair(String lexString, List<SyntaxKind> expected) {
        List<SyntaxKind> recieved = getSyntaxKinds(getTokens(lexString));
        recieved = recieved.subList(0, recieved.size() - 1);

        Assertions.assertEquals(expected, recieved);
    }

    /**
     * Checks if the two syntax kinds require a separator between them to be lexed as different tokens
     * @param left The left token
     * @param right The right token
     * @return True if the two syntax kinds require a separator, false otherwise
     */
    private static boolean requiresSeparator(SyntaxKind left, SyntaxKind right) {
        if (left == SyntaxKind.IdentifierToken || right == SyntaxKind.IdentifierToken)
            return true;
        if (left == SyntaxKind.TrueKeyword || left == SyntaxKind.FalseKeyword)
            return true;
        if (right == SyntaxKind.TrueKeyword || right == SyntaxKind.FalseKeyword)
            return true;
        if ((left == SyntaxKind.IntegerToken || left == SyntaxKind.FloatToken)
                && (right == SyntaxKind.IntegerToken || right == SyntaxKind.FloatToken)) {
            return true;
        }
        if (left == SyntaxKind.EqualsToken && right == SyntaxKind.EqualsToken)
            return true;
        if (left == SyntaxKind.EqualsToken && right == SyntaxKind.DoubleEqualsToken)
            return true;
        if (left == SyntaxKind.BangToken && right == SyntaxKind.EqualsToken)
            return true;
        if (left == SyntaxKind.BangToken && right == SyntaxKind.DoubleEqualsToken)
            return true;

        return false;
    }

    /**
     * Gets the syntax kinds of a list of tokens
     * @param tokens The list of tokens
     * @return The list of syntax kinds
     */
    private ArrayList<SyntaxKind> getSyntaxKinds(ArrayList<Token> tokens) {
        ArrayList<SyntaxKind> kinds = new ArrayList<>();
        for (Token token : tokens)
            kinds.add(token.kind);
        return kinds;
    }

    /**
     * Gets the tokens of a string
     * @param lexString The string to be lexed
     * @return The list of tokens
     */
    private ArrayList<Token> getTokens(String lexString) {
        ArrayList<Token> tokens = new ArrayList<>();

        Lexer lexer = new Lexer(lexString);
        SyntaxToken token;
        do {
            token = lexer.lex();
            tokens.add(new Token(token));
        } while (token.getSyntaxKind() != SyntaxKind.EOFToken);

        return tokens;
    }

    /**
     * A private record to store a token and its kind
     */
    private record Token(SyntaxKind kind, String text) {
        @Override
        public String toString() {
            return kind + " '" + text + "'";
        }

        public Token(SyntaxToken token) {
            this(token.getSyntaxKind(), token.getText());
        }
    }

}
