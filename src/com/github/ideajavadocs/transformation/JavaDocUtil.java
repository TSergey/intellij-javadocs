package com.github.ideajavadocs.transformation;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocElements;
import com.github.ideajavadocs.model.JavaDocTag;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.javadoc.PsiDocMethodOrFieldRef;
import com.intellij.psi.impl.source.javadoc.PsiDocParamRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.javadoc.PsiDocToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaDocUtil {

    @NotNull
    public static String convertJavaDoc(@NotNull JavaDoc javadoc) {
        // TODO
        StringBuilder builder = new StringBuilder();
        builder.append(JavaDocElements.STARTING_ASTERISK.getPresentation());
        for (String description : javadoc.getDescription()) {
            builder.append(JavaDocElements.NEW_LINE.getPresentation());
            builder.append(JavaDocElements.LINE_START.getPresentation());
            builder.append(description);
        }
        builder.append(JavaDocElements.NEW_LINE.getPresentation());
        builder.append(JavaDocElements.LINE_START.getPresentation());
        for (Entry<String, JavaDocTag> entry : javadoc.getTags().entrySet()) {
            builder.append(JavaDocElements.NEW_LINE.getPresentation());
            builder.append(JavaDocElements.LINE_START.getPresentation());
            builder.append(JavaDocElements.TAG_START.getPresentation());
            builder.append(entry.getKey());
            builder.append(" ");
            if (StringUtils.isNotBlank(entry.getValue().getRefParam())) {
                builder.append(entry.getValue().getRefParam());
            } else if (StringUtils.isNotBlank(entry.getValue().getValue())) {
                builder.append(entry.getValue().getValue());
            }
            builder.append(" ");
            for (String description : entry.getValue().getDescription()) {
                builder.append(description);
            }
        }
        builder.append(JavaDocElements.NEW_LINE.getPresentation());
        builder.append(JavaDocElements.ENDING_ASTERISK.getPresentation());
        return builder.toString();
    }

    @NotNull
    public static JavaDoc mergeJavaDocs(@NotNull JavaDoc oldJavaDoc, @NotNull JavaDoc newJavaDoc) {
        // TODO improve merge code to fix errors when tags appear removed

        List<String> description = oldJavaDoc.getDescription();
        if (CollectionUtils.isEmpty(description)) {
            description = newJavaDoc.getDescription();
        }
        Map<String, JavaDocTag> tags = new LinkedHashMap<String, JavaDocTag>();
        Map<String, JavaDocTag> oldTags = oldJavaDoc.getTags();
        Map<String, JavaDocTag> newTags = newJavaDoc.getTags();
        for (Entry<String, JavaDocTag> entry : newTags.entrySet()) {
            String name = entry.getKey();
            JavaDocTag tag = entry.getValue();
            if (oldTags.containsKey(name)) {
                // the case when old tag exists
                JavaDocTag oldTag = oldTags.get(name);
                if (StringUtils.isBlank(oldTag.getValue()) &&
                        StringUtils.isBlank(oldTag.getRefParam())) {
                    // the case when old tag has been broken and should be restored
                    tags.put(name, tag);
                } else {
                    // the case when old tag is ok
                    tags.put(name, mergeJavaDocTag(oldTag, tag));
                }
            } else {
                // the case when old tag has been removed
                tags.put(name, tag);
            }
        }
        return new JavaDoc(description, tags);
    }

    @NotNull
    public static JavaDocTag mergeJavaDocTag(@NotNull JavaDocTag oldJavaDocTag, @NotNull JavaDocTag newJavaDocTag) {
        List<String> description = oldJavaDocTag.getDescription();
        if (CollectionUtils.isEmpty(description)) {
            description = newJavaDocTag.getDescription();
        }
        return new JavaDocTag(oldJavaDocTag.getRefParam(), oldJavaDocTag.getValue(), description);
    }

    @NotNull
    public static JavaDocTag createJavaDocTag(@NotNull PsiDocTag docTag) {
        return new JavaDocTag(
                findDocTagRefParam(docTag),
                findDocTagValue(docTag),
                findDocTagDescription(docTag));
    }

    @NotNull
    public static JavaDoc createJavaDoc(@NotNull PsiDocComment docComment) {
        return new JavaDoc(
                findDocDescription(docComment),
                findDocTags(docComment));
    }

    @NotNull
    public static List<String> findDocDescription(@NotNull PsiDocComment docComment) {
        List<String> descriptions = new LinkedList<String>();
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        for (PsiElement descriptionElement : descriptionElements) {
            descriptions.add(descriptionElement.getText());
        }
        return descriptions;
    }

    @NotNull
    public static Map<String, JavaDocTag> findDocTags(@NotNull PsiDocComment docComment) {
        Map<String, JavaDocTag> tags = new LinkedHashMap<String, JavaDocTag>();
        PsiDocTag[] docTags = docComment.getTags();
        for (PsiDocTag docTag : docTags) {
            tags.put(docTag.getName(), createJavaDocTag(docTag));
        }
        return tags;
    }

    @Nullable
    public static String findDocTagRefParam(@NotNull PsiDocTag docTag) {
        String refParam = null;
        for (PsiElement element : docTag.getDataElements()) {
            if (element instanceof PsiDocParamRef ||
                    element instanceof PsiDocMethodOrFieldRef) {
                refParam = element.getText();
                break;
            }
        }
        return refParam;
    }

    @Nullable
    public static String findDocTagValue(@NotNull PsiDocTag docTag) {
        String value = null;
        for (PsiElement element : docTag.getDataElements()) {
            if (element instanceof PsiDocTagValue) {
                value = element.getText();
                break;
            }
        }
        return value;
    }

    @NotNull
    public static List<String> findDocTagDescription(@NotNull PsiDocTag docTag) {
        List<String> descriptions = new LinkedList<String>();
        for (PsiElement element : docTag.getDataElements()) {
            if (element instanceof PsiDocToken) {
                descriptions.add(element.getText());
            }
        }
        return descriptions;
    }

}
