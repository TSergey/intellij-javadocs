package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.generator.JavaDocGenerator;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.github.setial.intellijjavadocs.template.DocTemplateManager;
import com.github.setial.intellijjavadocs.template.DocTemplateProcessor;
import com.github.setial.intellijjavadocs.utils.JavaDocUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.PomNamedTarget;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract java doc generator.
 *
 * @param <T> the type parameter
 * @author Sergey Timofiychuk
 */
public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    public static final String DATE_FORMAT = "dateFormat";

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

        JavaDocSettings configuration = settings.getConfiguration();
        if (configuration != null) {
            Mode mode = configuration.getGeneralSettings().getMode();
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

    /**
     * Gets settings.
     *
     * @return the settings
     */
    @NotNull
    protected JavaDocConfiguration getSettings() {
        return settings;
    }

    /**
     * Check whether javadoc should be generated.
     *
     * @param modifiers the modifiers
     * @return the boolean
     */
    protected boolean shouldGenerate(PsiModifierList modifiers) {
        return checkModifiers(modifiers, PsiModifier.PUBLIC, Visibility.PUBLIC) ||
                checkModifiers(modifiers, PsiModifier.PROTECTED, Visibility.PROTECTED) ||
                checkModifiers(modifiers, PsiModifier.PACKAGE_LOCAL, Visibility.DEFAULT) ||
                checkModifiers(modifiers, PsiModifier.PRIVATE, Visibility.PRIVATE);
    }

    /**
     * Gets default parameters used to build template.
     *
     * @param element the element
     * @return the default parameters
     */
    protected Map<String, Object> getDefaultParameters(PomNamedTarget element) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(DATE_FORMAT, "yyyy-MM-dd");
        params.put("element", element);
        params.put("name", getDocTemplateProcessor().buildDescription(element.getName(), true));
        params.put("partName", getDocTemplateProcessor().buildPartialDescription(element.getName()));
        params.put("splitNames", StringUtils.splitByCharacterTypeCamelCase(element.getName()));
        for (Map.Entry<String, String> variable: getDocTemplateManager().getVariables().entrySet()) {
            params.put(variable.getKey(), variable.getValue());
        }
        params.put("todayDate", DateFormatUtils.format(new Date(), params.get(DATE_FORMAT).toString()));
        return params;
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

    private boolean checkModifiers(PsiModifierList modifiers, String modifier, Visibility visibility) {
        JavaDocSettings configuration = getSettings().getConfiguration();
        return modifiers != null && modifiers.hasModifierProperty(modifier) && configuration != null &&
                configuration.getGeneralSettings().getVisibilities().contains(visibility);
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
