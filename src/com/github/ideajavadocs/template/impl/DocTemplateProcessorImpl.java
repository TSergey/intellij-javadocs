package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.intellij.openapi.components.ProjectComponent;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * The type Doc template processor impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateProcessorImpl implements DocTemplateProcessor, ProjectComponent {

    private static final String REPLACEMENT_TOKENS_PATH = "/replacements.xml";

    // TODO move the section to the configuration menu
    private static final Map<String, String> REPLACE_TOKENS = new HashMap<String, String>();

    @Override
    public void projectOpened() {
        // populate REPLACE_TOKENS with default values
        try {
            Document document = new SAXBuilder().build(DocTemplateProcessor.class.getResourceAsStream
                    (REPLACEMENT_TOKENS_PATH));
            Element root = document.getRootElement();
            List<Element> items = root.getChildren("item");
            for (Element item : items) {
                REPLACE_TOKENS.put(item.getChild("input").getValue(), item.getChild("output").getValue());
            }
        } catch (JDOMException e) {
            // ignore error if settings can not be parsed
        } catch (IOException e) {
            // ignore error if settings can not be parsed
        }
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
    public String merge(@NotNull String template, @NotNull Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            template = template.replaceAll(getReplaceParameter(entry.getKey()), entry.getValue());
        }
        return template;

    }

    @NotNull
    @Override
    public String buildDescription(@NotNull String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                if (REPLACE_TOKENS.containsKey(parts[i])) {
                    result.append(REPLACE_TOKENS.get(parts[i]));
                } else {
                    result.append(StringUtils.capitalize(parts[i]));
                }
            } else {
                result.append(StringUtils.lowerCase(parts[i]));
            }
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    @NotNull
    @Override
    public String buildRawDescription(@NotNull String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description);
        StringBuilder result = new StringBuilder();
        int firstElement = 0;
        if (REPLACE_TOKENS.containsKey(parts[0])) {
            firstElement++;
        }
        for (int i = firstElement; i < parts.length; i++) {
            if (i == firstElement) {
                result.append(StringUtils.capitalize(parts[i]));
            } else {
                result.append(StringUtils.lowerCase(parts[i]));
            }
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    @NotNull
    private String getReplaceParameter(@NotNull String paramName) {
        return Pattern.quote("${" + paramName + "}");
    }

}
