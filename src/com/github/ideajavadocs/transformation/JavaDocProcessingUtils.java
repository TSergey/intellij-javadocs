package com.github.ideajavadocs.transformation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JavaDocProcessingUtils.
 *
 * @author Sergiy_Tymofiychuk
 */
public class JavaDocProcessingUtils {

    @NotNull
    public static String simpleDescription(@Nullable String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description);
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                parts[i] = StringUtils.capitalize(parts[i]);
            } else {
                parts[i] = StringUtils.lowerCase(parts[i]);
            }
        }
        return StringUtils.join(parts, " ");
    }

}
