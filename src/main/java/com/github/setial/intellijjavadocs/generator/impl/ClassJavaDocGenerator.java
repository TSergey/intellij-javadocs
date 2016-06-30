package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.utils.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import freemarker.template.Template;
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
        JavaDocSettings configuration = getSettings().getConfiguration();
        if ((configuration != null && !configuration.getGeneralSettings().getLevels().contains(Level.TYPE)) ||
                !shouldGenerate(element.getModifierList())) {
            return null;
        }
        Template template = getDocTemplateManager().getClassTemplate(element);
        Map<String, Object> params = getDefaultParameters(element);
        if (!configuration.getGeneralSettings().isSplittedClassName()) {
            params.put("name", element.getName());
        }
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

}
