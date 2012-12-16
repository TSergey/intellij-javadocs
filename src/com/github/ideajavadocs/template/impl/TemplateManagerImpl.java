package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.TemplateManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import org.jetbrains.annotations.NotNull;

public class TemplateManagerImpl implements TemplateManager, ProjectComponent {

    public static final String COMPONENT_NAME = "TemplateManager";

    // TODO add more templates for different cases

    /*     */
    private static final String CLASS_TEMPLATE =
            "/**\n" +
            " * The ${type} ${name}.\n" +
            " */";

    /*     */
    private static final String FIELD_TEMPLATE =
            "/**\n" +
            " * The ${name}.\n" +
            " */";
    private static final String CONSTANT_TEMPLATE =
            "/**\n" +
            " * The constant ${name}.\n" +
            " */";

    /*     */
    private static final String METHOD_TEMPLATE =
            "/**\n" +
            " * ${description}\n" +
            " */";
    private static final String CONSTRUCTOR_TEMPLATE =
            "/**\n" +
            " * Instantiates a new ${name}\n" +
            " */";

    // TODO setup access to the system settings where templates will be placed
    // TODO read template from file or app settings

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;

    }

    @NotNull
    @Override
    public String getClassTemplate(@NotNull PsiClass classElement) {
        return CLASS_TEMPLATE;

    }

    @NotNull
    @Override
    public String getMethodTemplate(@NotNull PsiMethod methodElement) {
        String result = METHOD_TEMPLATE;
        if (methodElement.isConstructor()) {
            result = CONSTRUCTOR_TEMPLATE;
        }
        return result;

    }

    @NotNull
    @Override
    public String getFieldTemplate(@NotNull PsiField fieldElement) {
        String result = FIELD_TEMPLATE;
        if (fieldElement.hasModifierProperty(PsiModifier.STATIC) &&
                fieldElement.hasModifierProperty(PsiModifier.FINAL)) {
            result = CONSTANT_TEMPLATE;
        }
        return result;

    }

    @NotNull
    @Override
    public String getParamTagTemplate(@NotNull PsiMethod fieldElement) {
        // TODO implement method body
        return null;

    }

    @NotNull
    @Override
    public String getReturnTagTemplate(@NotNull PsiMethod fieldElement) {
        // TODO implement method body
        return null;

    }

    @NotNull
    @Override
    public String getExceptionTagTemplate(@NotNull PsiMethod fieldElement) {
        // TODO implement method body
        return null;

    }

}
