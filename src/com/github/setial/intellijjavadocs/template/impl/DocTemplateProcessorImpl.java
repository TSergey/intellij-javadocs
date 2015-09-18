package com.github.setial.intellijjavadocs.template.impl;

import com.github.setial.intellijjavadocs.template.DocTemplateProcessor;
import com.github.setial.intellijjavadocs.utils.XmlUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * The type Doc template processor impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateProcessorImpl implements DocTemplateProcessor {

    // TODO move the logic to utils classes

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
    public String merge(@Nullable Template template, @NotNull Map<String, Object> params) {
        if (template == null) {
            // TODO throw exception and catch it at top level of app
            return StringUtils.EMPTY;
        }

        StringWriter writer = new StringWriter();
        try {
            template.process(params, writer);
            return XmlUtils.normalizeTemplate(writer.toString());
        } catch (IOException e) {
            // TODO throw runtime exception and catch it at top level app
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            // TODO throw runtime exception and catch it at top level app
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public String buildDescription(@NotNull String description, boolean capitalizeFirst) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        return buildDescription(description, 0, capitalizeFirst);
    }

    @NotNull
    @Override
    public String buildPartialDescription(@NotNull String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        return buildDescription(description, 1, false);
    }

    private String buildDescription(String description, int firstElement, boolean capitalizeFirst) {
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description.replaceAll("<.+>", ""));
        parts = removeInterfacePrefix(parts);
        parts = removeClassSuffix(parts);
        StringBuilder result = new StringBuilder();
        for (int i = firstElement; i < parts.length; i++) {
            if (capitalizeFirst && i == firstElement) {
                result.append(StringUtils.capitalize(StringUtils.lowerCase(parts[i])));
            } else {
                result.append(StringUtils.lowerCase(parts[i]));
            }
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    private String[] removeInterfacePrefix(String[] parts) {
        if (parts!= null && parts.length > 0 && "I".equalsIgnoreCase(parts[0])) {
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        return parts;
    }

    private String[] removeClassSuffix(String[] parts) {
        if (parts!= null && parts.length > 0 && "Impl".equalsIgnoreCase(parts[parts.length - 1])) {
            parts = Arrays.copyOfRange(parts, 0, parts.length - 1);
        }
        return parts;
    }

}
