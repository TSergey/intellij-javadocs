package com.github.setial.intellijjavadocs.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The interface Doc template manager.
 *
 * @author Sergey Timofiychuk
 */
public interface DocTemplateManager {

    /**
     * The constant COMPONENT_NAME.
     */
    String COMPONENT_NAME = "DocTemplateManager";

    /**
     * Gets the class template.
     *
     * @param classElement the Class element
     * @return the Class template
     */
    @NotNull
    Template getClassTemplate(@NotNull PsiClass classElement);

    @NotNull
    Map<String, String> getClassTemplates();

    void putClassTemplate(@NotNull String regexp, @NotNull String template);

    /**
     * Gets the method template.
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @NotNull
    Template getMethodTemplate(@NotNull PsiMethod methodElement);

    @NotNull
    Map<String, String> getMethodTemplates();

    void putMethodTemplates(@NotNull String regexp, @NotNull String template);

    @NotNull
    Map<String, String> getConstructorTemplates();

    void putConstructorTemplates(@NotNull String regexp, @NotNull String template);

    /**
     * Gets the field template.
     *
     * @param psiField the Field element
     * @return the Field template
     */
    @NotNull
    Template getFieldTemplate(@NotNull PsiField psiField);

    @NotNull
    Map<String, String> getFieldTemplates();

    void putFieldTemplates(@NotNull String regexp, @NotNull String template);

}
