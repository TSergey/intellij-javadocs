package com.github.setial.intellijjavadocs.ui.settings;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigPanel implements SearchableConfigurable, Disposable {

    private JavaDocConfiguration javaDocConfiguration;
    private ConfigPanelGUI configPanelGUI;
    private ResourceBundle resourceBundle;

    public ConfigPanel(@NotNull Project project) {
        javaDocConfiguration = project.getService(JavaDocConfiguration.class);
        resourceBundle = ResourceBundle.getBundle("messages");
    }

    @NotNull
    @Override
    public String getId() {
        return "JavaDoc";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return resourceBundle.getString("configurable.panel.name");
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
