package com.github.ideajavadocs.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface TemplateManager {

    @NotNull
    String getClassTemplate(@NotNull PsiClass classElement);

    @NotNull
    String getMethodTemplate(@NotNull PsiMethod methodElement);

    @NotNull
    String getFieldTemplate(@NotNull PsiField fieldElement);

    @NotNull
    String getParamTagTemplate(@NotNull PsiMethod fieldElement);

    @NotNull
    String getReturnTagTemplate(@NotNull PsiMethod fieldElement);

    @NotNull
    String getExceptionTagTemplate(@NotNull PsiMethod fieldElement);

}
