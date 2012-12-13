package com.github.ideajavadocs.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JavaDocTag {

    private String refParam;
    private String value;
    private List<String> description;

    public JavaDocTag(@Nullable String refParam, @Nullable String value, @NotNull List<String> description) {
        this.refParam = refParam;
        this.value = value;
        this.description = description;
    }

    @Nullable
    public String getRefParam() {
        return refParam;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    @NotNull
    public List<String> getDescription() {
        return description;
    }

}
