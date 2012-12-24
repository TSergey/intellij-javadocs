package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateManager;
import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
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
    private static final Map<Integer, Template> CLASS_TEMPLATES = new TreeMap<Integer, Template>();
    private static final Map<Integer, Template> FIELD_TEMPLATES = new TreeMap<Integer, Template>();
    private static final Map<Integer, Template> METHOD_TEMPLATES = new TreeMap<Integer, Template>();
    private static final Map<Integer, Template> CONSTRUCTOR_TEMPLATES = new TreeMap<Integer, Template>();
    private static final Map<Integer, Template> PARAM_TAG_TEMPLATES = new TreeMap<Integer, Template>();
    private static final Map<Integer, Template> THROWS_TAG_TEMPLATES = new TreeMap<Integer, Template>();

    private final RuntimeServices velosityServices;

    public DocTemplateManagerImpl() {
        velosityServices = RuntimeSingleton.getRuntimeServices();
    }

    @Override
    public void projectOpened() {
        try {
            Document document = new SAXBuilder().build(DocTemplateProcessor.class.getResourceAsStream
                    (TEMPLATES_PATH));
            Element root = document.getRootElement();
            if (root != null) {
                readTemplates(root, CLASS, CLASS_TEMPLATES);
                readTemplates(root, FIELD, FIELD_TEMPLATES);
                readTemplates(root, METHOD, METHOD_TEMPLATES);
                readTemplates(root, CONSTRUCTOR, CONSTRUCTOR_TEMPLATES);
                readTemplates(root, PARAM_TAG, PARAM_TAG_TEMPLATES);
                readTemplates(root, THROWS_TAG, THROWS_TAG_TEMPLATES);
            }
        } catch (Exception e) {
            // ignore error if settings can not be parsed
        }
    }

    private void readTemplates(Element document, String elementName, Map<Integer, Template> templates)
            throws DataConversionException, ParseException {
        Element root = document.getChild(elementName);
        @SuppressWarnings("unchecked")
        List<Element> elements = root.getChildren(TEMPLATE);
        for (Element element : elements) {
            SimpleNode node = velosityServices.parse(
                    StringUtils.strip(element.getValue()),
                    element.getAttribute(REGEXP).getValue());
            Template template = new Template();
            template.setRuntimeServices(velosityServices);
            template.setData(node);
            template.setName(node.getTemplateName());
            template.initDocument();

            templates.put(element.getAttribute(ORDER).getIntValue(), template);
        }
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
    public Template getClassTemplate(@NotNull PsiClass classElement) {
        return getMatchingTemplate(classElement.getText(), CLASS_TEMPLATES);
    }

    @NotNull
    @Override
    public Template getMethodTemplate(@NotNull PsiMethod methodElement) {
        Map<Integer, Template> templates;
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
    public Template getFieldTemplate(@NotNull PsiField psiField) {
        return getMatchingTemplate(psiField.getText(), FIELD_TEMPLATES);

    }

    @NotNull
    @Override
    public Template getParamTagTemplate(@NotNull PsiParameter psiParameter) {
        return getMatchingTemplate(psiParameter.getText(), PARAM_TAG_TEMPLATES);

    }

    @NotNull
    @Override
    public Template getExceptionTagTemplate(@NotNull PsiJavaCodeReferenceElement psiReferenceElement) {
        return getMatchingTemplate(psiReferenceElement.getCanonicalText(), THROWS_TAG_TEMPLATES);

    }

    @Nullable
    private Template getMatchingTemplate(@NotNull String elementText, @NotNull Map<Integer, Template> templates) {
        Template result = null;
        for (Template template : templates.values()) {
            if (Pattern.compile(template.getName(), Pattern.DOTALL | Pattern.MULTILINE).matcher(elementText).matches()) {
                result = template;
                break;
            }
        }
        return result;
    }

}
