package com.github.setial.intellijjavadocs.transformation;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.JavaDocElements;
import com.github.setial.intellijjavadocs.model.JavaDocTag;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
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
        builder.append(JavaDocElements.STARTING.getPresentation());
        builder.append(JavaDocElements.LINE_START.getPresentation());
        return this;
    }

    /**
     * Close the java doc section.
     *
     * @return the Java doc builder
     */
    public JavaDocBuilder closeJavaDoc() {
        if (builder.lastIndexOf(JavaDocElements.LINE_START.getPresentation()) != builder.length() - 1 &&
                builder.lastIndexOf(JavaDocElements.NEW_LINE.getPresentation()) >= 0) {
            builder.append(JavaDocElements.NEW_LINE.getPresentation());
            builder.append(JavaDocElements.WHITE_SPACE.getPresentation());
            builder.append(JavaDocElements.LINE_START.getPresentation());
        }
        builder.append(JavaDocElements.ENDING.getPresentation());
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
            if (StringUtils.isNotBlank(description) || StringUtils.contains(description, "\n")) {
                builder.append(description);
                if (StringUtils.isWhitespace(description)) {
                    builder.append(JavaDocElements.LINE_START.getPresentation());
                }
            }
        }
        return this;
    }

    public JavaDocBuilder addTagDescription(List<String> descriptions) {
        for (int i = 0; i < descriptions.size(); i++) {
            String description = descriptions.get(i);
            if (StringUtils.isNotBlank(description)) {
                if (i != 0) {
                    addNewLine();
                }
                builder.append(description);
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
        builder.append(JavaDocElements.WHITE_SPACE.getPresentation());
        builder.append(JavaDocElements.TAG_START.getPresentation());
        builder.append(name);
        builder.append(JavaDocElements.WHITE_SPACE.getPresentation());

        if (StringUtils.isNotBlank(tag.getRefParam())) {
            builder.append(tag.getRefParam());
        } else if (StringUtils.isNotBlank(tag.getValue())) {
            builder.append(tag.getValue());
        }

        builder.append(JavaDocElements.WHITE_SPACE.getPresentation());
        addTagDescription(tag.getDescription());
        return this;
    }

    /**
     * Add tags to javadoc section.
     *
     * @param tags the Tags
     * @return the Java doc builder
     */
    public JavaDocBuilder addTags(@NotNull Map<String, List<JavaDocTag>> tags) {
        Iterator<Entry<String, List<JavaDocTag>>> iterator = tags.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, List<JavaDocTag>> entry = iterator.next();
            String name = entry.getKey();
            Iterator<JavaDocTag> javaDocTagsIterator = entry.getValue().iterator();
            while (javaDocTagsIterator.hasNext()) {
                JavaDocTag javaDocTag = javaDocTagsIterator.next();
                addTag(name, javaDocTag);
                if (javaDocTagsIterator.hasNext()) {
                    addNewLine();
                }
            }
            if (iterator.hasNext()) {
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
        addTags(javadoc.getTags());
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
