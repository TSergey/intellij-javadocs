package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Class java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public class ClassJavaDocGenerator extends AbstractJavaDocGenerator<PsiClass> {

    /**
     * Instantiates a new Class java doc generator.
     *
     * @param project the Project
     */
    public ClassJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @Nullable
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiClass element) {
        if (!getSettings().getConfiguration().getLevels().contains(Level.TYPE) ||
                !shouldGenerate(element.getModifierList())) {
            return null;
        }
        String template = getDocTemplateManager().getClassTemplate(element);
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
        params.put("name", getDocTemplateProcessor().buildDescription(element.getName()));
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
