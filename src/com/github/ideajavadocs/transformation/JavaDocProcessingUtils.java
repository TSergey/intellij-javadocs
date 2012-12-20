package com.github.ideajavadocs.transformation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * JavaDocProcessingUtils.
 *
 * @author Sergiy_Tymofiychuk
 */
public class JavaDocProcessingUtils {

    public static final String GET = "get";
    public static final String SET = "set";
    public static final String CREATE = "create";

    public static final List<String> REMOVE_TOKENS = Arrays.asList(GET, SET, CREATE);

    @NotNull
    public static String simpleDescription(@Nullable String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        int firstElement = 0;
        // Detect if first element should be removed
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description);
        if (REMOVE_TOKENS.contains(parts[0])) {
            firstElement  ++;
        }
        StringBuilder result = new StringBuilder();
        for (int i = firstElement; i < parts.length; i++) {
            if (i == firstElement) {
                result.append(StringUtils.capitalize(parts[i]));
            } else {
                result.append(StringUtils.lowerCase(parts[i]));
            }
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

}
