package com.github.ideajavadocs.ui.component.impl;

import com.github.ideajavadocs.model.JavaDocSettings;
import com.github.ideajavadocs.ui.ConfigPanel;
import com.github.ideajavadocs.ui.component.JavaDocConfiguration;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The type Java doc configuration impl.
 *
 * @author Sergey Timofiychuk
 */
@State(
        name = JavaDocConfiguration.COMPONENT_NAME,
        storages = {
                @Storage(
                        id = "other",
                        file = StoragePathMacros.PROJECT_FILE
                )
        }
)
public class JavaDocConfigurationImpl implements JavaDocConfiguration, ProjectComponent, Configurable,
        PersistentStateComponent<JavaDocSettings> {

    private JavaDocSettings settings;
    private ConfigPanel configPanel;

    /**
     * Instantiates a new Java doc configuration impl.
     */
    public JavaDocConfigurationImpl() {
        settings = new JavaDocSettings();
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "JavaDoc Generator";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (configPanel == null) {
            configPanel = new ConfigPanel();
        }
        reset();
        return configPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
        configPanel = null;
    }

    @Override
    public JavaDocSettings getConfiguration() {
        return getState();
    }

    @Nullable
    @Override
    public JavaDocSettings getState() {
        return settings;
    }

    @Override
    public void loadState(JavaDocSettings javaDocSettings) {
        settings = javaDocSettings;
    }

}
