package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class DocTemplateManagerImpl implements DocTemplateManager, ProjectComponent {

    public static final String COMPONENT_NAME = "DocTemplateManager";

    // TODO add more templates for different cases

    /*     */
    private static final String CLASS_TEMPLATE =
            "/**\n" +
            " * The ${type} ${name}.\n" +
            " */";
    private static final Map<String, String> CLASS_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        CLASS_TEMPLATES.put(".+", CLASS_TEMPLATE);
    }

    /*     */
    private static final String FIELD_TEMPLATE =
            "/**\n" +
            " * The ${name}.\n" +
            " */";
    private static final String CONSTANT_TEMPLATE =
            "/**\n" +
            " * The constant ${name}.\n" +
            " */";
    private static final Map<String, String> FIELD_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        FIELD_TEMPLATES.put(".+", CONSTANT_TEMPLATE);
        FIELD_TEMPLATES.put(".+static\\s.+", FIELD_TEMPLATE);
    }

    /*     */
    private static final String METHOD_GETTER_TEMPLATE =
            "/**\n" +
            " * Gets the ${getter_description}.\n" +
            " * @return the ${getter_description}\n" +
            " */";
    private static final String METHOD_SETTER_TEMPLATE =
            "/**\n" +
            " * Sets the ${setter_description}.\n" +
            " */";
    private static final String METHOD_VOID_TEMPLATE =
            "/**\n" +
            " * ${description}.\n" +
            " */";
    private static final String METHOD_TEMPLATE =
            "/**\n" +
            " * ${description}.\n" +
            " * @return the ${return_description}" +
            " */";
    private static final Map<String, String> METHOD_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        METHOD_TEMPLATES.put(".*get.+", METHOD_GETTER_TEMPLATE);
        METHOD_TEMPLATES.put(".*set.+", METHOD_SETTER_TEMPLATE);
        METHOD_TEMPLATES.put(".*void\\s.+", METHOD_VOID_TEMPLATE);
        METHOD_TEMPLATES.put(".+", METHOD_TEMPLATE);
    }

    /*     */
    private static final String CONSTRUCTOR_TEMPLATE =
            "/**\n" +
            " * Instantiates a new ${description}.\n" +
            " */";
    private static final Map<String, String> CONSTRUCTOR_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        CONSTRUCTOR_TEMPLATES.put(".+", CONSTRUCTOR_TEMPLATE);
    }

    /*     */
    private static final String PARAM_TAG_TEMPLATE =
            "/**\n" +
            " * @param ${name} the ${description}\n" +
            " */";
    private static final Map<String, String> PARAM_TAG_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        PARAM_TAG_TEMPLATES.put(".+", PARAM_TAG_TEMPLATE);
    }

    /*     */
    private static final String THROWS_TAG_TEMPLATE =
            "/**\n" +
            " * @throws ${name} the ${description}\n" +
            " */";
    private static final Map<String, String> THROWS_TAG_TEMPLATES = new LinkedHashMap<String, String>();
    static {
        THROWS_TAG_TEMPLATES.put(".+", THROWS_TAG_TEMPLATE);
    }

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
        return getMatchingTemplate(classElement.getText(), CLASS_TEMPLATES);
    }

    @NotNull
    @Override
    public String getMethodTemplate(@NotNull PsiMethod methodElement) {
        Map<String, String> templates;
        if (methodElement.isConstructor()) {
            templates = CONSTRUCTOR_TEMPLATES;
        } else {
            templates = METHOD_TEMPLATES;
        }
        String signature = StringUtils.split(methodElement.getText(), "\n")[0];
        return getMatchingTemplate(signature, templates);

    }

    @NotNull
    @Override
    public String getFieldTemplate(@NotNull PsiField fieldElement) {
        return getMatchingTemplate(fieldElement.getName(), FIELD_TEMPLATES);

    }

    @NotNull
    @Override
    public String getParamTagTemplate(@NotNull PsiParameter psiParameter) {
        return getMatchingTemplate(psiParameter.getText(), PARAM_TAG_TEMPLATES);

    }

    @NotNull
    @Override
    public String getExceptionTagTemplate(@NotNull PsiClassType classElement) {
        return getMatchingTemplate(classElement.getCanonicalText(), THROWS_TAG_TEMPLATES);

    }

    @NotNull
    private String getMatchingTemplate(@NotNull String elementText, @NotNull Map<String, String> templates) {
        String result = StringUtils.EMPTY;
        for (Entry<String, String> entry : templates.entrySet()) {
            if (Pattern.compile(entry.getKey(), Pattern.DOTALL  | Pattern.MULTILINE).matcher(elementText).matches()) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

}
