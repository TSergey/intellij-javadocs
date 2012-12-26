package com.github.ideajavadocs.template;

import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
     *
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
     * @return the String
     */
    @NotNull
    String buildDescription(@NotNull String description);

}
