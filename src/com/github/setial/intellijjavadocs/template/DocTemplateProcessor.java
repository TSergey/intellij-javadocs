package com.github.setial.intellijjavadocs.template;

import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The interface Doc template processor.
 */
public interface DocTemplateProcessor {

    /**
     * The constant COMPONENT_NAME.
     */
    String COMPONENT_NAME = "DocTemplateProcessor";

    /**
     * Merge.
     *
     * @param template the Template
     * @param params   the Params
     * @return the String
     */
    @NotNull
    String merge(@Nullable Template template, @NotNull Map<String, Object> params);

    /**
     * Builds the description.
     *
     * @param description the Description
     * @return generated description
     */
    @NotNull
    String buildDescription(@NotNull String description);

    /**
     * Builds the description for the methods like getter, setter. There will be removed first word, e.g. get, set,
     * etc.
     *
     * @param description the description
     * @return generated description
     */
    @NotNull
    String buildPartialDescription(@NotNull String description);

}
