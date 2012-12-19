package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ClassJavaDocGenerator extends AbstractJavaDocGenerator<PsiClass> {

    public ClassJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @NotNull
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiClass element) {
        String template = getTemplateManager().getClassTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        String type;
        if (element.isAnnotationType()) {
            type = "annotation";
        } else if (element.isEnum()) {
            type = "enum";
        } else if (element.isInterface()) {
            type = "interface";
        } else {
            type = "type";
        }
        params.put("type", type);
        params.put("name", element.getName());
        String javaDocText = getTemplateProcessor().process(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
