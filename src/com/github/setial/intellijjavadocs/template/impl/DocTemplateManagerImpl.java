package com.github.setial.intellijjavadocs.template.impl;

import com.github.setial.intellijjavadocs.exception.SetupTemplateException;
import com.github.setial.intellijjavadocs.exception.TemplateNotFoundException;
import com.github.setial.intellijjavadocs.template.DocTemplateManager;
import com.github.setial.intellijjavadocs.template.DocTemplateProcessor;
import com.github.setial.intellijjavadocs.utils.XmlUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
public class DocTemplateManagerImpl implements DocTemplateManager {

    private static final Logger LOGGER = Logger.getInstance(DocTemplateManagerImpl.class);

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

    private Configuration config;
    private StringTemplateLoader templateLoader;

    /**
     * Instantiates a new Doc template manager object.
     */
    public DocTemplateManagerImpl() {
        templateLoader = new StringTemplateLoader();
        config = new Configuration();
        config.setDefaultEncoding("UTF-8");
        config.setLocalizedLookup(false);
        config.setTemplateLoader(templateLoader);
    }

    @Override
    public void initComponent() {
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
            e.printStackTrace();
            LOGGER.error(e);
        }
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

    @NotNull
    @Override
    public Map<String, String> getClassTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : classTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getConstructorTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : constructorTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getMethodTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : methodTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @NotNull
    @Override
    public Map<String, String> getFieldTemplates() {
        Map<String, String> templates = new LinkedHashMap<String, String>();
        for (Entry<String, Template> entry : fieldTemplates.entrySet()) {
            String template = extractTemplate(entry.getValue());
            templates.put(entry.getKey(), template);
        }
        return templates;
    }

    @Override
    public void setClassTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, classTemplates, CLASS);
    }

    @Override
    public void setConstructorTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, constructorTemplates, CONSTRUCTOR);
    }

    @Override
    public void setMethodTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, methodTemplates, METHOD);
    }

    @Override
    public void setFieldTemplates(@NotNull Map<String, String> templates) {
        setupTemplates(templates, fieldTemplates, FIELD);
    }

    private void readTemplates(Element document, String elementName, Map<String, Template> templates)
            throws IOException {
        Element root = document.getChild(elementName);
        List<Element> elements = root.getChildren(TEMPLATE);
        for (Element element : elements) {
            String name = element.getAttribute(REGEXP).getValue();
            templates.put(name, createTemplate(name, elementName, XmlUtils.trimElementContent(element)));
        }
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
        if (result == null) {
            throw new TemplateNotFoundException(elementText);
        }
        return result;
    }

    private void setupTemplates(Map<String, String> from, Map<String, Template> to, String elementName) {
        if (from != null && !from.isEmpty()) {
            Map<String, Template> result = new LinkedHashMap<String, Template>();
            for (Entry<String, String> entry : from.entrySet()) {
                try {
                    result.put(entry.getKey(), createTemplate(entry.getKey(), elementName, entry.getValue()));
                } catch (Exception e) {
                    throw new SetupTemplateException(e);
                }
            }
            to.clear();
            to.putAll(result);
        }
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

    private Template createTemplate(String templateRegexp, String elementName, String templateContent) throws IOException {
        String templateName = normalizeName(elementName + templateRegexp);
        if (templateLoader.findTemplateSource(templateName) != null) {
            config.clearTemplateCache();
        }
        templateLoader.putTemplate(templateName, templateContent);
        return config.getTemplate(templateName);
    }

    private String normalizeName(String templateName) {
        String result = templateName.replaceAll("\\*", "_");
        result = result.replaceAll("\\.", "_");
        return result;
    }

    private String extractTemplate(Template templateData) {
        Writer writer = new StringWriter();
        try {
            templateData.dump(writer);
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
        return writer.toString();
    }

}
