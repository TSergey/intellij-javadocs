package com.github.ideajavadocs.transformation;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocTag;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
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

public class JavaDocUtils {

    @NotNull
    public static String convertJavaDoc(@NotNull JavaDoc javadoc) {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.createDefaultJavaDoc(javadoc);
        return builder.build();
    }

    @NotNull
    public static JavaDoc mergeJavaDocs(@NotNull JavaDoc oldJavaDoc, @NotNull JavaDoc newJavaDoc) {
        // TODO improve merge code to fix errors when tags appear removed

        List<String> description = oldJavaDoc.getDescription();
        if (CollectionUtils.isEmpty(description)) {
            description = newJavaDoc.getDescription();
        }
        Map<String, List<JavaDocTag>> tags = new LinkedHashMap<String, List<JavaDocTag>>();
        Map<String, List<JavaDocTag>> oldTags = oldJavaDoc.getTags();
        Map<String, List<JavaDocTag>> newTags = newJavaDoc.getTags();
        for (Entry<String, List<JavaDocTag>> newTagsEntry : newTags.entrySet()) {
            String name = newTagsEntry.getKey();
            List<JavaDocTag> tagsEntry = newTagsEntry.getValue();
            for (JavaDocTag tag : tagsEntry) {
                if (!tags.containsKey(name)) {
                    tags.put(name, new LinkedList<JavaDocTag>());
                }
                if (oldTags.containsKey(name)) {
                    // the case when old tag exists
                    List<JavaDocTag> oldTagsEntry = oldTags.get(name);
                    for (JavaDocTag oldTag : oldTagsEntry) {
                        if (StringUtils.isBlank(oldTag.getValue()) &&
                                StringUtils.isBlank(oldTag.getRefParam())) {
                            // the case when old tag has been broken and should be restored
                            tags.get(name).add(tag);
                        } else {
                            // the case when old tag is ok
                            tags.get(name).add(mergeJavaDocTag(oldTag, tag));
                        }
                    }
                } else {
                    // the case when old tag has been removed
                    tags.get(name).add(tag);
                }
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
    public static Map<String, List<JavaDocTag>> findDocTags(@NotNull PsiDocComment docComment) {
        Map<String, List<JavaDocTag>> tags = new LinkedHashMap<String, List<JavaDocTag>>();
        PsiDocTag[] docTags = docComment.getTags();
        for (PsiDocTag docTag : docTags) {
            String name = docTag.getName();
            if (!tags.containsKey(name)) {
                tags.put(name, new LinkedList<JavaDocTag>());
            }
            tags.get(name).add(createJavaDocTag(docTag));
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

    @NotNull
    public static JavaDoc toJavaDoc(@NotNull String javaDocText, @NotNull PsiElementFactory psiElementFactory) {
        PsiDocComment javaDocComment = psiElementFactory.createDocCommentFromText(javaDocText);
        return createJavaDoc(javaDocComment);
    }
}
