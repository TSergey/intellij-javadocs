package com.github.setial.intellijjavadocs.template;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The interface Doc template manager.
 *
 * @author Sergey Timofiychuk
 */
public interface DocTemplateManager extends ApplicationComponent {

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

    /**
     * Gets class templates.
     *
     * @return the class templates
     */
    @NotNull
    Map<String, String> getClassTemplates();

    /**
     * Sets class templates.
     *
     * @param templates the templates
     */
    void setClassTemplates(@NotNull Map<String, String> templates);

    /**
     * Gets the method template.
     *
     * @param methodElement the Method element
     * @return the Method template
     */
    @Nullable
    Template getMethodTemplate(@NotNull PsiMethod methodElement);

    /**
     * Gets method templates.
     *
     * @return the method templates
     */
    @NotNull
    Map<String, String> getMethodTemplates();

    /**
     * Sets method templates.
     *
     * @param templates the templates
     */
    void setMethodTemplates(@NotNull Map<String, String> templates);

    /**
     * Gets constructor templates.
     *
     * @return the constructor templates
     */
    @NotNull
    Map<String, String> getConstructorTemplates();

    /**
     * Sets constructor templates.
     *
     * @param templates the templates
     */
    void setConstructorTemplates(@NotNull Map<String, String> templates);

    /**
     * Gets the field template.
     *
     * @param psiField the Field element
     * @return the Field template
     */
    @Nullable
    Template getFieldTemplate(@NotNull PsiField psiField);

    /**
     * Gets field templates.
     *
     * @return the field templates
     */
    @NotNull
    Map<String, String> getFieldTemplates();

    /**
     * Sets field templates.
     *
     * @param templates the templates
     */
    void setFieldTemplates(@NotNull Map<String, String> templates);

	/**
	 * Gets variables.
	 *
	 * @return the variables
	 */
	@NotNull
	Map<String, String> getVariables();

	/**
	 * Sets variables.
	 *
	 * @param variables the variables
	 */
	void setVariables(@NotNull Map<String, String> variables);

}
