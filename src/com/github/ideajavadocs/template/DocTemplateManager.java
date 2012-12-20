package com.github.ideajavadocs.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

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
    String getClassTemplate(@NotNull PsiClass classElement);

    /**
     * Gets the method template.
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @NotNull
    String getMethodTemplate(@NotNull PsiMethod methodElement);

    /**
     * Gets the field template.
     *
     * @param fieldElement the Field element
     * @return the Field template
     */
    @NotNull
    String getFieldTemplate(@NotNull PsiField fieldElement);

    /**
     * Gets the param tag template.
     *
     * @param fieldElement the Field element
     * @return the Param tag template
     */
    @NotNull
    String getParamTagTemplate(@NotNull PsiParameter fieldElement);

    /**
     * Gets the exception tag template.
     *
     * @param fieldElement the Field element
     * @return the Exception tag template
     */
    @NotNull
    String getExceptionTagTemplate(@NotNull PsiClassType fieldElement);

}
