package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateManager;
import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.github.ideajavadocs.transformation.XmlUtils;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * The type Doc template manager impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateManagerImpl implements DocTemplateManager, ProjectComponent {

    private static final String TEMPLATES_PATH = "/templates.xml";
    private static final String TEMPLATE = "template";
    private static final String REGEXP = "regexp";
    private static final String CLASS = "class";
    private static final String FIELD = "field";
    private static final String METHOD = "method";
    private static final String CONSTRUCTOR = "constructor";

    // TODO add more templates for different cases
    private static final Map<String, Template> CLASS_TEMPLATES = new LinkedHashMap<String, Template>();
    private static final Map<String, Template> FIELD_TEMPLATES = new LinkedHashMap<String, Template>();
    private static final Map<String, Template> METHOD_TEMPLATES = new LinkedHashMap<String, Template>();
    private static final Map<String, Template> CONSTRUCTOR_TEMPLATES = new LinkedHashMap<String, Template>();

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
            }
        } catch (Exception e) {
            // TODO throw runtime exception and catch it at top level app
            throw new RuntimeException(e);
        }
    }

    private void readTemplates(Element document, String elementName, Map<String, Template> templates)
            throws IOException, DataConversionException, ParseException {
        Element root = document.getChild(elementName);
        @SuppressWarnings("unchecked")
        List<Element> elements = root.getChildren(TEMPLATE);
        for (Element element : elements) {
            String name = element.getAttribute(REGEXP).getValue();
            SimpleNode node = velosityServices.parse(
                    XmlUtils.trimElementContent(element),
                    name);
            Template template = new Template();
            template.setRuntimeServices(velosityServices);
            template.setData(node);
            template.setName(node.getTemplateName());
            template.initDocument();

            templates.put(name, template);
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

    @Nullable
    @Override
    public Template getClassTemplate(@NotNull PsiClass classElement) {
        StringBuilder builder = getClassSignature(classElement);
        return getMatchingTemplate(builder.toString(), CLASS_TEMPLATES);
    }

    @Nullable
    @Override
    public Template getMethodTemplate(@NotNull PsiMethod methodElement) {
        Map<String, Template> templates;
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

    @Nullable
    @Override
    public Template getFieldTemplate(@NotNull PsiField psiField) {
        return getMatchingTemplate(psiField.getText(), FIELD_TEMPLATES);

    }

    @Nullable
    private Template getMatchingTemplate(@NotNull String elementText, @NotNull Map<String, Template> templates) {
        Template result = null;
        for (Entry<String, Template> entry : templates.entrySet()) {
            if (Pattern.compile(entry.getKey(), Pattern.DOTALL | Pattern.MULTILINE).matcher(elementText).matches()) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    private StringBuilder getClassSignature(PsiClass classElement) {
        StringBuilder builder = new StringBuilder();
        builder.append(classElement.getModifierList().getText());
        builder.append(" ");
        if (classElement.isInterface()) {
            builder.append("interface ");
        } else if (classElement.isEnum()) {
            builder.append("enum ");
        } else {
            builder.append("class ");
        }
        PsiClassType[] extendsTypes = classElement.getExtendsListTypes();
        if (extendsTypes != null && extendsTypes.length > 0) {
            builder.append("extends ");
            for (int i = 0; i < extendsTypes.length; i++) {
                PsiClassType extendsType = extendsTypes[i];
                builder.append(extendsType.getClassName());
                if (i < extendsTypes.length - 1) {
                    builder.append(",");
                }
                builder.append(" ");
            }
        }
        PsiClassType[] implementTypes = classElement.getImplementsListTypes();
        if (implementTypes != null && implementTypes.length > 0) {
            builder.append("implements");
            for (int i = 0; i < implementTypes.length; i++) {
                PsiClassType extendsType = extendsTypes[i];
                builder.append(extendsType.getClassName());
                if (i < extendsTypes.length - 1) {
                    builder.append(",");
                }
                builder.append(" ");
            }
        }
        return builder;
    }

}
