package com.github.setial.intellijjavadocs.model;

import com.github.setial.intellijjavadocs.transformation.JavaDocUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * The type Java doc.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDoc {

    private List<String> description;
    private Map<String, List<JavaDocTag>> tags;

    /**
     * Instantiates a new Java doc.
     *
     * @param description the Description
     * @param tags        the Tags
     */
    public JavaDoc(@NotNull List<String> description, @NotNull Map<String, List<JavaDocTag>> tags) {
        this.description = description;
        this.tags = tags;
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

    /**
     * Gets the tags.
     *
     * @return the Tags
     */
    @NotNull
    public Map<String, List<JavaDocTag>> getTags() {
        return tags;
    }

    /**
     * To java doc.
     *
     * @return the String
     */
    @NotNull
    public String toJavaDoc() {
        return JavaDocUtils.convertJavaDoc(this);
    }

}
