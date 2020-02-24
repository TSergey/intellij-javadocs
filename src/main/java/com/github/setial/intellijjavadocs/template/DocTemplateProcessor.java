package com.github.setial.intellijjavadocs.template;

import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The interface Doc template processor.
 *
 * @author Sergey Timofiychuk
 */
public interface DocTemplateProcessor {

    /**
     * Merge.
     *
     * @param template the Template
     * @param params   the Params
     * @return the String
     */
    @NotNull
    String merge(@NotNull Template template, @NotNull Map<String, Object> params);

    /**
     * Builds the description.
     *
     * @param description     the Description
     * @param capitalizeFirst the flag shows whether first word should be capitalized
     * @return generated description
     */
    @NotNull
    String buildDescription(@NotNull String description, boolean capitalizeFirst);

    /**
     * Builds the description for the methods like getter, setter. There will be removed first word, e.g. get, set,
     * etc.
     *
     * @param description the description
     * @return generated description
     */
    @NotNull
    String buildPartialDescription(@NotNull String description);

    /**
     * Builds the description for the methods like getter, setter. The result will be a field name for get/set method.
     *
     * @param description the description
     * @return generated description
     */
    @NotNull
    String buildFieldDescription(@NotNull String description);

}
