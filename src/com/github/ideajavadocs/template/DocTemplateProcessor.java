package com.github.ideajavadocs.template;

import org.jetbrains.annotations.NotNull;

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
    String merge(@NotNull String template, @NotNull Map<String, String> params);

    /**
     * Builds the description.
     *
     * @param description the Description
     * @return the String
     */
    @NotNull
    String buildDescription(@NotNull String description);

    /**
     * Builds the raw description.
     *
     * @param description the Description
     * @return the String
     */
    @NotNull
    String buildRawDescription(@NotNull String description);

}
