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
import org.apache.commons.collections.MapUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.ParseException;
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
    private DocTemplateProcessor templateProcessor;

    public DocTemplateManagerImpl(Project project) {
        templateProcessor = ServiceManager.getService(project, DocTemplateProcessor.class);
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
            throws IOException, DataConversionException, ParseException {
        Element root = document.getChild(elementName);
        @SuppressWarnings("unchecked")
        List<Element> elements = root.getChildren(TEMPLATE);
        for (Element element : elements) {
            String name = element.getAttribute(REGEXP).getValue();
            SimpleNode node = velocityServices.parse(
                    XmlUtils.trimElementContent(element),
                    name);
            Template template = new Template();
            template.setRuntimeServices(velocityServices);
            template.setData(node);
            template.setName(node.getTemplateName());
            template.initDocument();

            templates.put(name, template);
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
        return getMatchingTemplate(builder.toString(), classTemplates);
    }

    @NotNull
    @Override
    public Map<String, String> getClassTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : classTemplates.entrySet()) {
            templates.put(entry.getKey(), templateProcessor.merge(entry.getValue(), MapUtils.EMPTY_MAP));
        }
        return Collections.unmodifiableMap(templates);
    }

    @Override
    public void putClassTemplate(@NotNull String regexp, @NotNull String template) {
        // TODO
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

    @NotNull
    @Override
    public Map<String, String> getMethodTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : methodTemplates.entrySet()) {
            templates.put(entry.getKey(), templateProcessor.merge(entry.getValue(), MapUtils.EMPTY_MAP));
        }
        return Collections.unmodifiableMap(templates);
    }

    @Override
    public void putMethodTemplates(@NotNull String regexp, @NotNull String template) {
        // TODO
    }

    @NotNull
    @Override
    public Map<String, String> getConstructorTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : constructorTemplates.entrySet()) {
            templates.put(entry.getKey(), templateProcessor.merge(entry.getValue(), MapUtils.EMPTY_MAP));
        }
        return Collections.unmodifiableMap(templates);
    }

    @Override
    public void putConstructorTemplates(@NotNull String regexp, @NotNull String template) {
        // TODO
    }

    @Nullable
    @Override
    public Template getFieldTemplate(@NotNull PsiField psiField) {
        return getMatchingTemplate(psiField.getText(), fieldTemplates);

    }

    @NotNull
    @Override
    public Map<String, String> getFieldTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : fieldTemplates.entrySet()) {
            templates.put(entry.getKey(), templateProcessor.merge(entry.getValue(), MapUtils.EMPTY_MAP));
        }
        return Collections.unmodifiableMap(templates);
    }

    @Override
    public void putFieldTemplates(@NotNull String regexp, @NotNull String template) {
        // TODO
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

}
