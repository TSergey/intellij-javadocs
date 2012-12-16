package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MethodJavaDocGenerator extends AbstractJavaDocGenerator<PsiMethod> {

    public MethodJavaDocGenerator(Project project) {
        super(project);
    }

    @NotNull
    @Override
    protected String getTemplate(@NotNull PsiMethod element) {
        return getTemplateManager().getMethodTemplate(element);

    }

    @NotNull
    @Override
    protected Map<String, String> getParams(@NotNull PsiMethod element) {
        Map<String, String> params = new HashMap<String, String>();
        String name = element.getName();
        String[] description = StringUtils.splitByCharacterTypeCamelCase(name);
        params.put("description", StringUtils.join(description, " "));
        params.put("name", name);
        return params;

    }

    @Override
    protected JavaDoc enrichJavaDoc(@NotNull JavaDoc newJavaDoc) {
        // TODO process tags, exceptions and return value
        return newJavaDoc;
    }

}
