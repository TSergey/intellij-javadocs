package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.utils.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        JavaDocSettings configuration = getSettings().getConfiguration();
        if (configuration != null && !configuration.getGeneralSettings().getLevels().contains(Level.FIELD) ||
                !shouldGenerate(element.getModifierList())) {
            return null;
        }
        Template template = getDocTemplateManager().getFieldTemplate(element);
        Map<String, Object> params = getDefaultParameters(element);
        PsiClass parent = findClassElement(element);
        if (parent != null) {
            params.put("typeName", getDocTemplateProcessor().buildDescription(parent.getName(), false));
        }
        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

    private PsiClass findClassElement(PsiElement element) {
        PsiClass parentClass = null;
        if (element != null) {
            PsiElement parent = element.getParent();
            if (parent instanceof PsiClass) {
                parentClass = (PsiClass) parent;
            } else {
                parentClass = findClassElement(parent);
            }
        }
        return parentClass;
    }

}
