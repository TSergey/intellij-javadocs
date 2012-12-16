package com.github.ideajavadocs.template.impl;

import com.github.ideajavadocs.template.TemplateProcessor;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class TemplateProcessorImpl implements TemplateProcessor, ApplicationComponent {

    public static final String COMPONENT_NAME = "TemplateProcessor";

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
    public String process(@NotNull String template, @NotNull Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            template = template.replaceAll(getReplaceParameter(entry.getKey()), entry.getValue());
        }
        return template;

    }

    @NotNull
    private String getReplaceParameter(@NotNull String paramName) {
        return Pattern.quote("${" + paramName + "}");
    }

}
