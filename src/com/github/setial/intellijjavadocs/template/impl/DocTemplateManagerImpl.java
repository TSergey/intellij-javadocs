package com.github.setial.intellijjavadocs.template.impl;

import com.github.setial.intellijjavadocs.template.DocTemplateManager;
import com.github.setial.intellijjavadocs.template.DocTemplateProcessor;
import com.github.setial.intellijjavadocs.utils.XmlUtils;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.ParserConstants;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
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

    private Map<String, Template> classTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> fieldTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> methodTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> constructorTemplates = new LinkedHashMap<String, Template>();

    private final RuntimeServices velocityServices;

    public DocTemplateManagerImpl() {
        RuntimeInstance ri = new RuntimeInstance();
        ri.init();
        velocityServices = ri;

        try {
            Document document = new SAXBuilder().build(DocTemplateProcessor.class.getResourceAsStream
                    (TEMPLATES_PATH));
            Element root = document.getRootElement();
            if (root != null) {
                readTemplates(root, CLASS, classTemplates);
                readTemplates(root, FIELD, fieldTemplates);
                readTemplates(root, METHOD, methodTemplates);
                readTemplates(root, CONSTRUCTOR, constructorTemplates);
            }
        } catch (Exception e) {
            // TODO throw runtime exception and catch it at top level app
            throw new RuntimeException(e);
        }
    }

    private void readTemplates(Element document, String elementName, Map<String, Template> templates)
            throws IOException, ParseException {
        Element root = document.getChild(elementName);
        @SuppressWarnings("unchecked")
        List<Element> elements = root.getChildren(TEMPLATE);
        for (Element element : elements) {
            String name = element.getAttribute(REGEXP).getValue();
            templates.put(name, createTemplate(name, XmlUtils.trimElementContent(element)));
        }
    }

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

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public Template getClassTemplate(@NotNull PsiClass classElement) {
        StringBuilder builder = getClassSignature(classElement);
        return getMatchingTemplate(builder.toString(), classTemplates);
    }

    @Nullable
    @Override
    public Template getMethodTemplate(@NotNull PsiMethod methodElement) {
        Map<String, Template> templates;
        if (methodElement.isConstructor()) {
            templates = constructorTemplates;
        } else {
            templates = methodTemplates;
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
        return getMatchingTemplate(psiField.getText(), fieldTemplates);

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

    @NotNull
    @Override
    public Map<String, String> getClassTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : classTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue().getData());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getConstructorTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : constructorTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue().getData());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getMethodTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : methodTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue().getData());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getFieldTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : fieldTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue().getData());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @Override
    public void setClassTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, classTemplates);
    }

    @Override
    public void setConstructorTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, constructorTemplates);
    }

    @Override
    public void setMethodTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, methodTemplates);
    }

    @Override
    public void setFieldTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, fieldTemplates);
    }

    private void setupTemplates(Map<String, String> from, Map<String, Template> to) {
        Map<String, Template> result = new LinkedHashMap<String, Template>();
        for (Entry<String, String> entry : from.entrySet()) {
            try {
                result.put(entry.getKey(), createTemplate(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                // TODO throw runtime exception and catch it at top level app
                throw new RuntimeException(e);
            }
        }
        to.clear();
        to.putAll(result);
    }

    private StringBuilder getClassSignature(PsiClass classElement) {
        StringBuilder builder = new StringBuilder();
        PsiModifierList modifierList = classElement.getModifierList();
        if (modifierList != null) {
            builder.append(modifierList.getText());
        }
        builder.append(" ");
        if (classElement.isInterface()) {
            builder.append("interface ");
        } else if (classElement.isEnum()) {
            builder.append("enum ");
        } else {
            builder.append("class ");
        }
        builder.append(classElement.getName());
        builder.append(" ");
        PsiClassType[] extendsTypes = classElement.getExtendsListTypes();
        if (extendsTypes.length > 0) {
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
        if (implementTypes.length > 0) {
            builder.append("implements");
            for (int i = 0; i < implementTypes.length; i++) {
                PsiClassType implementType = implementTypes[i];
                builder.append(implementType.getClassName());
                if (i < implementTypes.length - 1) {
                    builder.append(",");
                }
                builder.append(" ");
            }
        }
        return builder;
    }

    private Template createTemplate(String templateRegexp, String templateContent) throws ParseException {
        SimpleNode node = velocityServices.parse(templateContent, templateRegexp);
        Template template = new Template();
        template.setRuntimeServices(velocityServices);
        template.setData(node);
        template.setName(node.getTemplateName());
        template.initDocument();
        return template;
    }

    private String extractTemplate(Object data) {
        StringBuilder template = new StringBuilder();

        Token token = ((SimpleNode) data).getFirstToken();
        while (token != null && token.kind != ParserConstants.EOF) {
            template.append(token.toString());
            token = token.next;
        }
        return template.toString();
    }

}
