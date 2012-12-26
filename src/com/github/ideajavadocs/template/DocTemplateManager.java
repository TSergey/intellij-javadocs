package com.github.ideajavadocs.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Gets the method template.
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @NotNull
    Template getMethodTemplate(@NotNull PsiMethod methodElement);

    /**
     * Gets the field template.
     *
     * @param psiField the Field element
     * @return the Field template
     */
    @NotNull
    Template getFieldTemplate(@NotNull PsiField psiField);

}
