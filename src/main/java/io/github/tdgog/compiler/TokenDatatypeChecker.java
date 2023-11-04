package io.github.tdgog.compiler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A class to check the datatype of a token
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenDatatypeChecker {

    private static final BigInteger intMax = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigDecimal doubleMax = BigDecimal.valueOf(Double.MAX_VALUE);

    /**
     * Checks if a string is a valid integer
     * @param string The string to check
     * @return Whether the string is a valid integer
     */
    public static boolean isInteger(@NotNull String string) {
        if (string.contains("."))
            return false;

        return new BigInteger(string).compareTo(intMax) <= 0;
    }

    public static boolean isFloat(@NotNull String string) {
        if (string.charAt(0) == '.')
            string = '0' + string;

        return new BigDecimal(string).compareTo(doubleMax) <= 0;
    }

}
