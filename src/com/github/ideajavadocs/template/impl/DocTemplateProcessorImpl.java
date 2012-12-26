package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.github.ideajavadocs.transformation.XmlUtils;
import com.intellij.openapi.components.ProjectComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The type Doc template processor impl.
 *
 * @author Sergey Timofiychuk
 */
public class DocTemplateProcessorImpl implements DocTemplateProcessor, ProjectComponent {

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
        Context context = new VelocityContext(params);
        context.put("StringUtils", StringUtils.class);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        try {
            return XmlUtils.normalizeTemplate(writer.toString());
        } catch (IOException e) {
            // TODO throw runtime exception and catch it at top level app
            throw new RuntimeException(e);
        }
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
            result.append(StringUtils.capitalize(parts[i]));
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

}
