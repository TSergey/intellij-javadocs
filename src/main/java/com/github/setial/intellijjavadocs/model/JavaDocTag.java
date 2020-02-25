package com.github.setial.intellijjavadocs.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The type Java doc tag.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocTag {

    private String refParam;
    private String value;
    private List<String> description;

    /**
     * Instantiates a new Java doc tag.
     *
     * @param refParam    the Ref param
     * @param value       the Value
     * @param description the Description
     */
    public JavaDocTag(@Nullable String refParam, @Nullable String value, @NotNull List<String> description) {
        this.refParam = refParam;
        this.value = value;
        this.description = description;
    }

    /**
     * Gets the ref param.
     *
     * @return the Ref param
     */
    @Nullable
    public String getRefParam() {
        return refParam;
    }

    /**
     * Gets the value.
     *
     * @return the Value
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * Gets the description.
     *
     * @return the Description
     */
    @NotNull
    public List<String> getDescription() {
        return description;
    }

}
