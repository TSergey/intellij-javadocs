package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocTag;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiTypeElement;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

/**
 * The type Method java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public class MethodJavaDocGenerator extends AbstractJavaDocGenerator<PsiMethod> {

    /**
     * Instantiates a new Method java doc generator.
     *
     * @param project the Project
     */
    public MethodJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @Nullable
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiMethod element) {
        if (!shouldGenerate(element) || !shouldGenerate(element.getModifierList())) {
            return null;
        }
        String template = getDocTemplateManager().getMethodTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        String name = element.getName();
        String returnDescription = StringUtils.EMPTY;
        PsiTypeElement returnElement = element.getReturnTypeElement();
        if (returnElement != null) {
            returnDescription = returnElement.getText();
        }
        params.put("description", getDocTemplateProcessor().buildDescription(name));
        params.put("return_by_name", getDocTemplateProcessor().buildRawDescription(name));
        params.put("return_description", getDocTemplateProcessor().buildDescription(returnDescription));

        String javaDocText = getDocTemplateProcessor().merge(template, params);
        JavaDoc newJavaDoc = JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());

        Map<String, List<JavaDocTag>> tags = new LinkedHashMap<String, List<JavaDocTag>>();
        tags.putAll(newJavaDoc.getTags());
        processParamTags(element, tags);
        processExceptionTags(element, tags);
        return new JavaDoc(newJavaDoc.getDescription(), tags);
    }

    private void processExceptionTags(@NotNull PsiMethod element, @NotNull Map<String, List<JavaDocTag>> tags) {
        for (PsiClassType psiClassType : element.getThrowsList().getReferencedTypes()) {
            String template = getDocTemplateManager().getExceptionTagTemplate(psiClassType);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiClassType.getClassName();
            params.put("name", name);
            params.put("description", getDocTemplateProcessor().buildDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getDocTemplateProcessor().merge(template, params), getPsiElementFactory());
            addTags(javaDocEnrichment, tags);
        }
    }

    private void processParamTags(@NotNull PsiMethod element, @NotNull Map<String, List<JavaDocTag>> tags) {
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            String template = getDocTemplateManager().getParamTagTemplate(psiParameter);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiParameter.getName();
            params.put("name", name);
            params.put("description", getDocTemplateProcessor().buildDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getDocTemplateProcessor().merge(template, params), getPsiElementFactory());
            addTags(javaDocEnrichment, tags);
        }
    }

    private void addTags(JavaDoc javaDocEnrichment, Map<String, List<JavaDocTag>> tags) {
        for (Entry<String, List<JavaDocTag>> tagEntries : javaDocEnrichment.getTags().entrySet()) {
            String tagName = tagEntries.getKey();
            if (!tags.containsKey(tagName)) {
                tags.put(tagName, new LinkedList<JavaDocTag>());
            }
            tags.get(tagName).addAll(tagEntries.getValue());
        }
    }

    private boolean shouldGenerate(@NotNull PsiMethod element) {
        PsiMethod[] superMethods = element.findSuperMethods();
        boolean overriddenMethods = superMethods.length > 0 && !getSettings().getConfiguration().isOverriddenMethods();
        boolean level = getSettings().getConfiguration().getLevels().contains(Level.METHOD);
        return !level || !overriddenMethods;
    }

}
