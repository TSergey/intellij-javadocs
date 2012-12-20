package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocTag;
import com.github.ideajavadocs.transformation.JavaDocProcessingUtils;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiTypeElement;

import org.apache.commons.lang3.StringUtils;
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
        String template = getDocTemplateManager().getMethodTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        String name = element.getName();
        String returnDescription = StringUtils.EMPTY;
        PsiTypeElement returnElement = element.getReturnTypeElement();
        if(returnElement != null) {
            returnDescription = returnElement.getText();
        }
        params.put("description", JavaDocProcessingUtils.simpleDescription(name));
        params.put("getter_description", JavaDocProcessingUtils.simpleDescription(name));
        params.put("setter_description", JavaDocProcessingUtils.simpleDescription(name));
        params.put("return_description", JavaDocProcessingUtils.simpleDescription(returnDescription));

        String javaDocText = getDocTemplateProcessor().process(template, params);
        JavaDoc newJavaDoc = JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());

        Map<String, JavaDocTag> tags = new LinkedHashMap<String, JavaDocTag>();
        tags.putAll(newJavaDoc.getTags());
        processParamTags(element, tags);
        processExceptionTags(element, tags);
        return new JavaDoc(newJavaDoc.getDescription(), tags);
    }

    private void processExceptionTags(@NotNull PsiMethod element, @NotNull Map<String, JavaDocTag> tags) {
        for (PsiClassType psiClassType : element.getThrowsList().getReferencedTypes()) {
            String template = getDocTemplateManager().getExceptionTagTemplate(psiClassType);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiClassType.getClassName();
            params.put("name", name);
            params.put("description", JavaDocProcessingUtils.simpleDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getDocTemplateProcessor().process(template, params), getPsiElementFactory());
            tags.putAll(javaDocEnrichment.getTags());
        }
    }

    private void processParamTags(@NotNull PsiMethod element, @NotNull Map<String, JavaDocTag> tags) {
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            String template = getDocTemplateManager().getParamTagTemplate(psiParameter);
            Map<String, String> params = new HashMap<String, String>();
            String name = psiParameter.getName();
            params.put("name", name);
            params.put("description", JavaDocProcessingUtils.simpleDescription(name));
            JavaDoc javaDocEnrichment = JavaDocUtils.toJavaDoc(
                    getDocTemplateProcessor().process(template, params), getPsiElementFactory());
            tags.putAll(javaDocEnrichment.getTags());
        }
    }

}
