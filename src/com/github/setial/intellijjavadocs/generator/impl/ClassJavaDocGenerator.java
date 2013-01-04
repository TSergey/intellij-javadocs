package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (!getSettings().getConfiguration().getGeneralSettings().getLevels().contains(Level.TYPE) ||
                !shouldGenerate(element.getModifierList())) {
            return null;
        }
        Template template = getDocTemplateManager().getClassTemplate(element);
        Map<String, Object> params = getDefaultParameters(element);
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
