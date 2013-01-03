package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.settings.Mode;
import com.github.ideajavadocs.model.settings.Visibility;
import com.github.ideajavadocs.template.DocTemplateManager;
import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.github.ideajavadocs.configuration.JavaDocConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.PomNamedTarget;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.javadoc.PsiDocComment;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    private DocTemplateManager docTemplateManager;
    private DocTemplateProcessor docTemplateProcessor;
    private PsiElementFactory psiElementFactory;
    private JavaDocConfiguration settings;

    /**
     * Instantiates a new Abstract java doc generator.
     *
     * @param project the Project
     */
    public AbstractJavaDocGenerator(@NotNull Project project) {
        docTemplateManager = ServiceManager.getService(project, DocTemplateManager.class);
        docTemplateProcessor = ServiceManager.getService(project, DocTemplateProcessor.class);
        psiElementFactory = PsiElementFactory.SERVICE.getInstance(project);
        settings = ServiceManager.getService(project, JavaDocConfiguration.class);
    }

    @Nullable
    @Override
    public final PsiDocComment generate(@NotNull T element) {
        PsiDocComment result = null;
        PsiDocComment oldDocComment = null;
        PsiElement firstElement = element.getFirstChild();
        if (firstElement instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) firstElement;
        }

        Mode mode = settings.getConfiguration().getGeneralSettings().getMode();
        switch (mode) {
            case KEEP:
                if (oldDocComment != null) {
                    break;
                }
            case REPLACE:
                result = replaceJavaDocAction(element);
                break;
            case UPDATE:
            default:
                if (oldDocComment != null) {
                    result = updateJavaDocAction(element, oldDocComment);
                } else {
                    result = replaceJavaDocAction(element);
                }
                break;
        }
        return result;
    }

    private PsiDocComment updateJavaDocAction(T element, PsiDocComment oldDocComment) {
        PsiDocComment result = null;
        JavaDoc newJavaDoc = generateJavaDoc(element);
        JavaDoc oldJavaDoc = JavaDocUtils.createJavaDoc(oldDocComment);
        if (newJavaDoc != null) {
            newJavaDoc = JavaDocUtils.mergeJavaDocs(oldJavaDoc, newJavaDoc);
            String javaDoc = newJavaDoc.toJavaDoc();
            result = psiElementFactory.createDocCommentFromText(javaDoc);
        }
        return result;
    }

    private PsiDocComment replaceJavaDocAction(T element) {
        PsiDocComment result = null;
        JavaDoc newJavaDoc = generateJavaDoc(element);
        if (newJavaDoc != null) {
            String javaDoc = newJavaDoc.toJavaDoc();
            result = psiElementFactory.createDocCommentFromText(javaDoc);
        }
        return result;
    }

    /**
     * Gets the doc template manager.
     *
     * @return the Doc template manager
     */
    @NotNull
    protected DocTemplateManager getDocTemplateManager() {
        return docTemplateManager;
    }

    /**
     * Gets the doc template processor.
     *
     * @return the Doc template processor
     */
    @NotNull
    protected DocTemplateProcessor getDocTemplateProcessor() {
        return docTemplateProcessor;
    }

    /**
     * Gets the psi element factory.
     *
     * @return the Psi element factory
     */
    @NotNull
    protected PsiElementFactory getPsiElementFactory() {
        return psiElementFactory;
    }

    @NotNull
    protected JavaDocConfiguration getSettings() {
        return settings;
    }

    protected boolean shouldGenerate(PsiModifierList modifiers) {
        return checkModifiers(modifiers, PsiModifier.PUBLIC, Visibility.PUBLIC) ||
                checkModifiers(modifiers, PsiModifier.PROTECTED, Visibility.PROTECTED) ||
                checkModifiers(modifiers, PsiModifier.PACKAGE_LOCAL, Visibility.DEFAULT) ||
                checkModifiers(modifiers, PsiModifier.PRIVATE, Visibility.PRIVATE);
    }

    private boolean checkModifiers(PsiModifierList modifiers, String modifier, Visibility visibility) {
        return modifiers != null && modifiers.hasModifierProperty(modifier) &&
                getSettings().getConfiguration().getGeneralSettings().getVisibilities().contains(visibility);
    }

    protected Map<String, Object> getDefaultParameters(PomNamedTarget element) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("element", element);
        params.put("name", getDocTemplateProcessor().buildDescription(element.getName()));
        params.put("partName", getDocTemplateProcessor().buildPartialDescription(element.getName()));
        params.put("splitNames", StringUtils.splitByCharacterTypeCamelCase(element.getName()));
        return params;
    }

    /**
     * Generate java doc.
     *
     * @param element the Element
     * @return the Java doc
     */
    @Nullable
    protected abstract JavaDoc generateJavaDoc(@NotNull T element);

}
