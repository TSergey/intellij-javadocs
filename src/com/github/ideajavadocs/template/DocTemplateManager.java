package com.github.ideajavadocs.template;

import com.intellij.psi.*;

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
     *
     * @param classElement the Class element
     * @return the Class template
     */
    @NotNull
    Template getClassTemplate(@NotNull PsiClass classElement);

    /**
     * Gets the method template.
     *
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @NotNull
    Template getMethodTemplate(@NotNull PsiMethod methodElement);

    /**
     * Gets the field template.
     *
     *
     * @param psiField the Field element
     * @return the Field template
     */
    @NotNull
    Template getFieldTemplate(@NotNull PsiField psiField);

    /**
     * Gets the param tag template.
     *
     *
     * @param psiParameter the Field element
     * @return the Param tag template
     */
    @NotNull
    Template getParamTagTemplate(@NotNull PsiParameter psiParameter);

    /**
     * Gets the exception tag template.
     *
     *
     * @param psiReferenceElement the Psi reference element
     * @return the Exception tag template
     */
    @NotNull
    Template getExceptionTagTemplate(@NotNull PsiJavaCodeReferenceElement psiReferenceElement);

}
