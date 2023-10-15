package io.github.tdgog.compiler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * A class to check the datatype of a token
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenDatatypeChecker {

    /**
     * Checks if a string is a valid integer
     * @param string The string to check
     * @return Whether the string is a valid integer
     */
    public static boolean isInteger(@NotNull String string) {
        if (string.charAt(0) == '-')
            string = string.substring(1);

        for (char character : string.toCharArray())
            if (!Character.isDigit(character))
                return false;

        return true;
    }

}
