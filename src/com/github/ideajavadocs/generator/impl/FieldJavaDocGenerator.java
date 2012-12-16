package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    public FieldJavaDocGenerator(Project project) {
        super(project);
    }

    @NotNull
    @Override
    protected String getTemplate(@NotNull PsiField element) {
        return getTemplateManager().getFieldTemplate(element);
    }

    @NotNull
    @Override
    protected Map<String, String> getParams(@NotNull PsiField element) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", element.getName());
        return params;
    }

    @Override
    protected JavaDoc enrichJavaDoc(@NotNull JavaDoc newJavaDoc) {
        return newJavaDoc;
    }

}
