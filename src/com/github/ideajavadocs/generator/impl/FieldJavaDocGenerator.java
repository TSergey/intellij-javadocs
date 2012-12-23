package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Field java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    /**
     * Instantiates a new Field java doc generator.
     *
     * @param project the Project
     */
    public FieldJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @Nullable
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiField element) {
        if (!getSettings().getConfiguration().getLevels().contains(Level.FIELD) ||
                !shouldGenerate(element.getModifierList())) {
            return null;
        }
        String template = getDocTemplateManager().getFieldTemplate(element);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", element.getName());
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
