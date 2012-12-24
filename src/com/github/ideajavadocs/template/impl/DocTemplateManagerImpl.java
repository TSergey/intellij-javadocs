package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateManager;
import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * The type Doc template manager impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateManagerImpl implements DocTemplateManager, ProjectComponent {

    private static final String TEMPLATES_PATH = "/templates.xml";
    private static final String ORDER = "order";
    private static final String TEMPLATE = "template";
    private static final String REGEXP = "regexp";
    private static final String CLASS = "class";
    private static final String FIELD = "field";
    private static final String METHOD = "method";
    private static final String CONSTRUCTOR = "constructor";
    private static final String PARAM_TAG = "param-tag";
    private static final String THROWS_TAG = "throws-tag";

    // TODO add more templates for different cases
    private static final Map<String, String> CLASS_TEMPLATES = new LinkedHashMap<String, String>();
    private static final Map<String, String> FIELD_TEMPLATES = new LinkedHashMap<String, String>();
    private static final Map<String, String> METHOD_TEMPLATES = new LinkedHashMap<String, String>();
    private static final Map<String, String> CONSTRUCTOR_TEMPLATES = new LinkedHashMap<String, String>();
    private static final Map<String, String> PARAM_TAG_TEMPLATES = new LinkedHashMap<String, String>();
    private static final Map<String, String> THROWS_TAG_TEMPLATES = new LinkedHashMap<String, String>();

    @Override
    public void projectOpened() {
        try {
            Document document = new SAXBuilder().build(DocTemplateProcessor.class.getResourceAsStream
                    (TEMPLATES_PATH));
            Element root = document.getRootElement();
            if (root != null) {
                populateTemplates(root, CLASS, CLASS_TEMPLATES);
                populateTemplates(root, FIELD, FIELD_TEMPLATES);
                populateTemplates(root, METHOD, METHOD_TEMPLATES);
                populateTemplates(root, CONSTRUCTOR, CONSTRUCTOR_TEMPLATES);
                populateTemplates(root, PARAM_TAG, PARAM_TAG_TEMPLATES);
                populateTemplates(root, THROWS_TAG, THROWS_TAG_TEMPLATES);
            }
        } catch (Exception e) {
            // ignore error if settings can not be parsed
        }
    }

    private void populateTemplates(Element root, String elementName, Map<String, String> templates)
            throws DataConversionException {
        Map<Integer, Element> elements = readTemplates(root, elementName);
        for (Element element : elements.values()) {
            templates.put(element.getAttribute(REGEXP).getValue(), StringUtils.strip(element.getValue()));
        }
    }

    private Map<Integer, Element> readTemplates(Element root, String aClass) throws DataConversionException {
        Element element = root.getChild(aClass);
        @SuppressWarnings("unchecked")
        List<Element> templates = element.getChildren(TEMPLATE);
        Map<Integer, Element> elements = new TreeMap<Integer, Element>();
        for (Element template : templates) {
            elements.put(template.getAttribute(ORDER).getIntValue(), template);
        }
        return elements;
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {
        // TODO read template from file or app settings
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
        String signature = methodElement.getText();
        PsiCodeBlock methodBody = methodElement.getBody();
        if (methodBody != null) {
            signature = signature.replace(methodBody.getText(), "");
        }
        return getMatchingTemplate(signature, templates);

    }

    @NotNull
    @Override
    public String getFieldTemplate(@NotNull PsiField fieldElement) {
        return getMatchingTemplate(fieldElement.getText(), FIELD_TEMPLATES);

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
            if (Pattern.compile(entry.getKey(), Pattern.DOTALL | Pattern.MULTILINE).matcher(elementText).matches()) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

}
