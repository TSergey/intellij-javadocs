package com.github.setial.intellijjavadocs.utils;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.JavaDocTag;
import com.github.setial.intellijjavadocs.transformation.JavaDocBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.impl.source.javadoc.PsiDocMethodOrFieldRef;
import com.intellij.psi.impl.source.javadoc.PsiDocParamRef;
import com.intellij.psi.impl.source.javadoc.PsiDocTagValueImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The type Java doc utils.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocUtils {

    private static final List<String> MERGE_TAG_NAMES = Arrays.asList("param", "throws");

    /**
     * Convert java doc.
     *
     * @param javadoc the Javadoc
     * @return the String
     */
    @NotNull
    public static String convertJavaDoc(@NotNull JavaDoc javadoc) {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.createDefaultJavaDoc(javadoc);
        return builder.build();
    }

    /**
     * Merge java docs.
     *
     * @param oldJavaDoc the Old java doc
     * @param newJavaDoc the New java doc
     * @return the Java doc
     */
    @NotNull
    public static JavaDoc mergeJavaDocs(@NotNull JavaDoc oldJavaDoc, @NotNull JavaDoc newJavaDoc) {
        List<String> description = oldJavaDoc.getDescription();
        if (descriptionIsEmpty(description)) {
            description = newJavaDoc.getDescription();
        }
        Map<String, List<JavaDocTag>> oldTags = oldJavaDoc.getTags();
        Map<String, List<JavaDocTag>> newTags = newJavaDoc.getTags();
        Map<String, List<JavaDocTag>> tags = new LinkedHashMap<>();
        List<String> processedTagNames = new LinkedList<>();
        for (Entry<String, List<JavaDocTag>> newTagsEntry : newTags.entrySet()) {
            String name = newTagsEntry.getKey();
            if (!tags.containsKey(name)) {
                tags.put(name, new LinkedList<>());
            }
            List<JavaDocTag> tagsEntry = newTagsEntry.getValue();
            for (JavaDocTag tag : tagsEntry) {
                if (oldTags.containsKey(name)) {
                    // the case when old tag exists
                    List<JavaDocTag> oldTagsEntry = oldTags.get(name);
                    JavaDocTag oldTag;
                    if (!MERGE_TAG_NAMES.contains(name)) {
                        oldTag = oldTagsEntry.get(0);
                    } else {
                        oldTag = findOldTag(oldTagsEntry, tag.getValue(), tag.getRefParam());
                    }
                    if (oldTag != null) {
                        // the case when old tag is ok
                        tags.get(name).add(mergeJavaDocTag(oldTag, tag));
                    } else {
                        tags.get(name).add(tag);
                    }
                } else {
                    // the case when old tag has been removed
                    tags.get(name).add(tag);
                }
            }
            processedTagNames.add(name);
        }
        // add old tags that were not processed
        for (Entry<String, List<JavaDocTag>> entry : oldTags.entrySet()) {
            String name = entry.getKey();
            if (!processedTagNames.contains(name)) {
                tags.put(name, entry.getValue());
            }
        }
        return new JavaDoc(description, tags);
    }

    /**
     * Merge java doc tag.
     *
     * @param oldJavaDocTag the Old java doc tag
     * @param newJavaDocTag the New java doc tag
     * @return the Java doc tag
     */
    @NotNull
    public static JavaDocTag mergeJavaDocTag(@NotNull JavaDocTag oldJavaDocTag, @NotNull JavaDocTag newJavaDocTag) {
        List<String> description = oldJavaDocTag.getDescription();
        if (descriptionIsEmpty(description)) {
            description = newJavaDocTag.getDescription();
        }
        return new JavaDocTag(oldJavaDocTag.getRefParam(), oldJavaDocTag.getValue(), description);
    }

    /**
     * Creates the java doc tag.
     *
     * @param docTag the Doc tag
     * @return the Java doc tag
     */
    @NotNull
    public static JavaDocTag createJavaDocTag(@NotNull PsiDocTag docTag) {
        String docTagRefParam = findDocTagRefParam(docTag);
        String docTagValue = findDocTagValue(docTag);
        List<String> docTagDescription = findDocTagDescription(docTag, docTagRefParam, docTagValue);
        return new JavaDocTag(
                docTagRefParam,
                docTagValue,
                docTagDescription);
    }

    /**
     * Creates the java doc.
     *
     * @param docComment the Doc comment
     * @return the Java doc
     */
    @NotNull
    public static JavaDoc createJavaDoc(@NotNull PsiDocComment docComment) {
        return new JavaDoc(
                findDocDescription(docComment),
                findDocTags(docComment));
    }

    /**
     * Find java doc description.
     *
     * @param docComment the Doc comment
     * @return the description
     */
    @NotNull
    public static List<String> findDocDescription(@NotNull PsiDocComment docComment) {
        List<String> descriptions = new LinkedList<>();
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        for (PsiElement descriptionElement : descriptionElements) {
            descriptions.add(descriptionElement.getText());
        }
        return descriptions;
    }

    /**
     * Find doc tags.
     *
     * @param docComment the Doc comment
     * @return the javadoc tags
     */
    @NotNull
    public static Map<String, List<JavaDocTag>> findDocTags(@NotNull PsiDocComment docComment) {
        Map<String, List<JavaDocTag>> tags = new LinkedHashMap<>();
        PsiDocTag[] docTags = docComment.getTags();
        for (PsiDocTag docTag : docTags) {
            String name = docTag.getName();
            if (!tags.containsKey(name)) {
                tags.put(name, new LinkedList<>());
            }
            tags.get(name).add(createJavaDocTag(docTag));
        }
        return tags;
    }

    /**
     * Find doc tag ref param.
     *
     * @param docTag the Doc tag
     * @return the javadoc's tag ref parameter
     */
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

    /**
     * Find doc tag value.
     *
     * @param docTag the Doc tag
     * @return the javadoc's tag value
     */
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

    /**
     * Find doc tag description.
     *
     * @param docTag         the Doc tag
     * @param docTagRefParam the doc tag ref param
     * @param docTagValue    the doc tag value
     * @return the javadoc's tag descriptions
     */
    @NotNull
    public static List<String> findDocTagDescription(@NotNull PsiDocTag docTag, String docTagRefParam, String docTagValue) {
        List<String> descriptions = new LinkedList<>();
        List<PsiElement> elements = new LinkedList<>(Arrays.asList(docTag.getDataElements()));
        for (Iterator<PsiElement> iterator = elements.iterator(); iterator.hasNext(); ) {
            PsiElement element = iterator.next();
            removeValueIfAssignableType(docTagRefParam, PsiDocParamRef.class, iterator, element);
            removeValueIfAssignableType(docTagValue, PsiDocTagValueImpl.class, iterator, element);
        }
        StringBuilder descriptionBuilder = new StringBuilder();
        for (PsiElement element : elements) {
            descriptionBuilder.append(element.getText());
        }
        descriptions.add(descriptionBuilder.toString());
        return descriptions;
    }

    private static void removeValueIfAssignableType(String value, Class<? extends PsiElement> valueType,
                                                    Iterator<PsiElement> iterator, PsiElement element) {
        if (value != null && element.getClass().isAssignableFrom(valueType) && element.getText().equals(value)) {
            iterator.remove();
        }
    }

    /**
     * Converts string to java doc.
     *
     * @param javaDocText       the Java doc text
     * @param psiElementFactory the Psi element factory
     * @return the Java doc
     */
    @Nullable
    public static JavaDoc toJavaDoc(@Nullable String javaDocText, @NotNull PsiElementFactory psiElementFactory) {
        JavaDoc result = null;
        if (StringUtils.isNotBlank(javaDocText)) {
            PsiDocComment javaDocComment = psiElementFactory.createDocCommentFromText(javaDocText);
            result = createJavaDoc(javaDocComment);
        }
        return result;
    }

    @Nullable
    private static JavaDocTag findOldTag(List<JavaDocTag> oldTagsEntry, String value, String refParam) {
        JavaDocTag result = null;
        for (JavaDocTag oldTag : oldTagsEntry) {
            if (StringUtils.equals(oldTag.getValue(), value) &&
                    StringUtils.equals(oldTag.getRefParam(), refParam)) {
                result = oldTag;
                break;
            }
        }
        return result;
    }

    private static boolean descriptionIsEmpty(List<String> description) {
        boolean result = true;
        if (!CollectionUtils.isEmpty(description)) {
            for (String item : description) {
                result = result && StringUtils.isBlank(item);
            }
        }
        return result;
    }

    private JavaDocUtils() {
    }

}
