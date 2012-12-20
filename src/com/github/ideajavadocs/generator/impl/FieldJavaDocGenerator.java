package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    public FieldJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @NotNull
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiField element) {
        String template = getDocTemplateManager().getFieldTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", element.getName());
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
