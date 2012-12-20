package com.github.ideajavadocs.model;

import com.github.ideajavadocs.transformation.JavaDocUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class JavaDoc {

    private List<String> description;
    private Map<String, List<JavaDocTag>> tags;

    public JavaDoc(@NotNull List<String> description, @NotNull Map<String, List<JavaDocTag>> tags) {
        this.description = description;
        this.tags = tags;
    }

    @NotNull
    public List<String> getDescription() {
        return description;
    }

    @NotNull
    public Map<String, List<JavaDocTag>> getTags() {
        return tags;
    }

    @NotNull
    public String toJavaDoc() {
        return JavaDocUtils.convertJavaDoc(this);
    }

}
