package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocTag;
import com.github.ideajavadocs.transformation.JavaDocProcessingUtils;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodJavaDocGenerator extends AbstractJavaDocGenerator<PsiMethod> {

    public MethodJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @NotNull
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiMethod element) {
        String template = getTemplateManager().getMethodTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        String name = element.getName();
        params.put("description", JavaDocProcessingUtils.simpleDescription(name));
        params.put("name", name);

        String javaDocText = getTemplateProcessor().process(template, params);
        JavaDoc newJavaDoc = JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());

        Map<String, JavaDocTag> tags = new LinkedHashMap<String, JavaDocTag>();
        tags.putAll(newJavaDoc.getTags());
        processParamTags(element, tags);
        processExceptionTags(element, tags);
        return new JavaDoc(newJavaDoc.getDescription(), tags);
    }

    private void processExceptionTags(@NotNull PsiMethod element, @NotNull Map<String, JavaDocTag> tags) {
        for (PsiClassType psiClassType : element.getThrowsList().getReferencedTypes()) {
            String template = getTemplateManager().getExceptionTagTemplate(psiClassType);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiClassType.getClassName();
            params.put("name", name);
            params.put("description", JavaDocProcessingUtils.simpleDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getTemplateProcessor().process(template, params), getPsiElementFactory());
            tags.putAll(javaDocEnrichment.getTags());
        }
    }

    private void processParamTags(@NotNull PsiMethod element, @NotNull Map<String, JavaDocTag> tags) {
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            String template = getTemplateManager().getParamTagTemplate(psiParameter);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiParameter.getName();
            params.put("name", name);
            params.put("description", JavaDocProcessingUtils.simpleDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getTemplateProcessor().process(template, params), getPsiElementFactory());
            tags.putAll(javaDocEnrichment.getTags());
        }
    }

}
