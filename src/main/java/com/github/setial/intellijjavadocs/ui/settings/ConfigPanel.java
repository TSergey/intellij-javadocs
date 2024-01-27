package com.github.setial.intellijjavadocs.ui.settings;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class ConfigPanel implements SearchableConfigurable, Disposable {

    private JavaDocConfiguration javaDocConfiguration;
    private ConfigPanelGUI configPanelGUI;

    public ConfigPanel(@NotNull Project project) {
        javaDocConfiguration = project.getService(JavaDocConfiguration.class);
    }

    @NotNull
    @Override
    public String getId() {
        return "JavaDoc";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "JavaDoc";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        configPanelGUI = new ConfigPanelGUI(javaDocConfiguration.getConfiguration());
        configPanelGUI.reset();
        return configPanelGUI;
    }

    @Override
    public boolean isModified() {
        return configPanelGUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configPanelGUI.apply();
        javaDocConfiguration.setupTemplates();
    }

    @Override
    public void dispose() {
        configPanelGUI.disposeUIResources();
        configPanelGUI = null;
    }
}
