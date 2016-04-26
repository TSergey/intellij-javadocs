package com.github.setial.intellijjavadocs.template.impl;

import com.github.setial.intellijjavadocs.exception.SetupTemplateException;
import com.github.setial.intellijjavadocs.template.DocTemplateProcessor;
import com.github.setial.intellijjavadocs.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Doc template processor impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateProcessorImpl implements DocTemplateProcessor {

    private static final List<String> SPECIAL_SYMBOLS = Arrays.asList("_", "$");

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
    public String merge(@NotNull freemarker.template.Template template, @NotNull Map<String, Object> params) {
        StringWriter writer = new StringWriter();
        try {
            template.process(params, writer);
            return XmlUtils.normalizeTemplate(writer.toString());
        } catch (Exception e) {
            throw new SetupTemplateException(e);
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

    @NotNull
    @Override
    public String buildFieldDescription(@NotNull String description) {
        if (StringUtils.isBlank(description)) {
            return StringUtils.EMPTY;
        }
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description.replaceAll("<.+>", ""));
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (i > 1) {
                result.append(StringUtils.capitalize(parts[i]));
            } else {
                result.append(StringUtils.uncapitalize(parts[i]));
            }
        }
        return result.toString();
    }

    private String buildDescription(String description, int firstElement, boolean capitalizeFirst) {
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(description.replaceAll("<.+>", ""));
        parts = removeInterfacePrefix(parts);
        parts = removeClassSuffix(parts);
        parts = removeSpecialSymbols(parts);

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
        if (parts != null && parts.length > 0 && "I".equalsIgnoreCase(parts[0])) {
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        return parts;
    }

    private String[] removeClassSuffix(String[] parts) {
        if (parts != null && parts.length > 0 && "Impl".equalsIgnoreCase(parts[parts.length - 1])) {
            parts = Arrays.copyOfRange(parts, 0, parts.length - 1);
        }
        return parts;
    }

    private String[] removeSpecialSymbols(String[] parts) {
        List<String> result = new ArrayList<String>();
        for (String part : parts) {
            if (!SPECIAL_SYMBOLS.contains(part)) {
                result.add(part);
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
