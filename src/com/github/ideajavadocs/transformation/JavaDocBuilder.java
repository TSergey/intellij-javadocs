package com.github.ideajavadocs.transformation;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocElements;
import com.github.ideajavadocs.model.JavaDocTag;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The type Java doc builder.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocBuilder {

    private StringBuilder builder;

    /**
     * Instantiates a new Java doc builder.
     */
    public JavaDocBuilder() {
        builder = new StringBuilder();
    }

    /**
     * Open the java doc section.
     *
     * @return the Java doc builder
     */
    public JavaDocBuilder openJavaDoc() {
        builder.append(JavaDocElements.STARTING_ASTERISK.getPresentation());
        return this;
    }

    /**
     * Close the java doc section.
     *
     * @return the Java doc builder
     */
    public JavaDocBuilder closeJavaDoc() {
        builder.append(JavaDocElements.ENDING_ASTERISK.getPresentation());
        return this;
    }

    /**
     * Add new line to javadoc section.
     *
     * @return the Java doc builder
     */
    public JavaDocBuilder addNewLine() {
        builder.append(JavaDocElements.NEW_LINE.getPresentation());
        builder.append(JavaDocElements.LINE_START.getPresentation());
        return this;
    }

    /**
     * Add description to javadoc section.
     *
     * @param descriptions the Descriptions
     * @return the Java doc builder
     */
    public JavaDocBuilder addDescription(List<String> descriptions) {
        for (String description : descriptions) {
            if (!StringUtils.equals(description, " ")) {
                builder.append(description);
                if (StringUtils.isWhitespace(description)) {
                    builder.append(JavaDocElements.LINE_START.getPresentation());
                }
            }
        }
        return this;
    }

    /**
     * Add tag to javadoc section.
     *
     * @param name the Name
     * @param tag  the Tag
     * @return the Java doc builder
     */
    public JavaDocBuilder addTag(String name, JavaDocTag tag) {
        addNewLine();
        builder.append(JavaDocElements.TAG_START.getPresentation());
        builder.append(name);
        builder.append(JavaDocElements.WHITE_SPACE.getPresentation());

        if (StringUtils.isNotBlank(tag.getRefParam())) {
            builder.append(tag.getRefParam());
        } else if (StringUtils.isNotBlank(tag.getValue())) {
            builder.append(tag.getValue());
        }

        builder.append(JavaDocElements.WHITE_SPACE.getPresentation());
        for (String description : tag.getDescription()) {
            builder.append(description);
            addNewLine();
        }
        return this;
    }

    /**
     * Add tags to javadoc section.
     *
     * @param tags the Tags
     * @return the Java doc builder
     */
    public JavaDocBuilder addTags(@NotNull Map<String, List<JavaDocTag>> tags) {
        for (Entry<String, List<JavaDocTag>> entry : tags.entrySet()) {
            String name = entry.getKey();
            for (JavaDocTag javaDocTag : entry.getValue()) {
                addTag(name, javaDocTag);
                addNewLine();
            }
        }
        return this;
    }

    /**
     * Creates the java doc by default rules.
     *
     * @param javadoc the Javadoc
     * @return the Java doc builder
     */
    public JavaDocBuilder createDefaultJavaDoc(@NotNull JavaDoc javadoc) {
        openJavaDoc();
        addDescription(javadoc.getDescription());
        addNewLine();
        addTags(javadoc.getTags());
        addNewLine();
        closeJavaDoc();
        return this;
    }

    /**
     * Builds the javadoc section.
     *
     * @return the String
     */
    @NotNull
    public String build() {
        return builder.toString();
    }

}
