package com.github.setial.intellijjavadocs.template;

import com.intellij.openapi.components.ProjectComponent;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The interface Doc template processor.
 *
 * @author Sergey Timofiychuk
 */
public interface DocTemplateProcessor extends ProjectComponent {

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
     *
     * @param description the Description
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

}
