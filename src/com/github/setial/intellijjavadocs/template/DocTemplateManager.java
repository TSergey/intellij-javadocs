package com.github.setial.intellijjavadocs.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    Template getClassTemplate(@NotNull PsiClass classElement);

    @NotNull
    Map<String, String> getClassTemplates();

    void setClassTemplates(@NotNull Map<String, String> templates);

    /**
     * Gets the method template.
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @Nullable
    Template getMethodTemplate(@NotNull PsiMethod methodElement);

    @NotNull
    Map<String, String> getMethodTemplates();

    void setMethodTemplates(@NotNull Map<String, String> templates);

    @NotNull
    Map<String, String> getConstructorTemplates();

    void setConstructorTemplates(@NotNull Map<String, String> templates);

    /**
     * Gets the field template.
     *
     * @param psiField the Field element
     * @return the Field template
     */
    @Nullable
    Template getFieldTemplate(@NotNull PsiField psiField);

    @NotNull
    Map<String, String> getFieldTemplates();

    void setFieldTemplates(@NotNull Map<String, String> templates);

}
