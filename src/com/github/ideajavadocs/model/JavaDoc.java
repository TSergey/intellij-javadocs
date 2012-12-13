package com.github.ideajavadocs.model;

import com.github.ideajavadocs.transformation.JavaDocUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class JavaDoc {

    private List<String> description;
    private Map<String, JavaDocTag> tags;

    public JavaDoc(@NotNull List<String> description, @NotNull Map<String, JavaDocTag> tags) {
        this.description = description;
        this.tags = tags;
    }

    @NotNull
    public List<String> getDescription() {
        return description;
    }

    @NotNull
    public Map<String, JavaDocTag> getTags() {
        return tags;
    }

    @NotNull
    public String toJavaDoc() {
        return JavaDocUtil.convertJavaDoc(this);
    }

}
