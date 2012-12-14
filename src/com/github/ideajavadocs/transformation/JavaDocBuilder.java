package com.github.ideajavadocs.transformation;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocElements;
import com.github.ideajavadocs.model.JavaDocTag;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaDocBuilder {

    private StringBuilder builder;

    public JavaDocBuilder() {
        builder = new StringBuilder();
    }

    public JavaDocBuilder openJavaDoc() {
        builder.append(JavaDocElements.STARTING_ASTERISK);
        return this;
    }

    public JavaDocBuilder closeJavaDoc() {
        builder.append(JavaDocElements.ENDING_ASTERISK);
        return this;
    }

    public JavaDocBuilder addNewLine() {
        builder.append(JavaDocElements.NEW_LINE);
        builder.append(JavaDocElements.LINE_START);
        return this;
    }

    public JavaDocBuilder addDescription(List<String> descriptions) {
        for (String description : descriptions) {
            builder.append(JavaDocElements.NEW_LINE.getPresentation());
            builder.append(JavaDocElements.LINE_START.getPresentation());
            builder.append(description);
        }
        return this;
    }

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

    public JavaDocBuilder addTags(@NotNull Map<String, JavaDocTag> tags) {
        for (Entry<String, JavaDocTag> entry : tags.entrySet()) {
            addTag(entry.getKey(), entry.getValue());
            addNewLine();
        }
        return this;
    }

    public JavaDocBuilder createDefaultJavaDoc(@NotNull JavaDoc javadoc) {
        openJavaDoc();
        addNewLine();
        addDescription(javadoc.getDescription());
        addNewLine();
        addTags(javadoc.getTags());
        addNewLine();
        closeJavaDoc();
        return this;
    }

    @NotNull
    public String build() {
        return builder.toString();
    }

}
