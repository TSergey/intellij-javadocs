package com.github.ideajavadocs.ui.component.impl;

import com.github.ideajavadocs.model.settings.JavaDocSettings;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.model.settings.Mode;
import com.github.ideajavadocs.model.settings.Visibility;
import com.github.ideajavadocs.ui.ConfigPanel;
import com.github.ideajavadocs.ui.component.JavaDocConfiguration;
import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

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
        PersistentStateComponent<Element> {

    private JavaDocSettings settings;
    private ConfigPanel configPanel;
    private boolean loadedStoredConfig = false;

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
            configPanel = new ConfigPanel(getSettings());
        }
        reset();
        return configPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        return configPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configPanel.apply();
    }

    @Override
    public void reset() {
        configPanel.reset();
    }

    @Override
    public void disposeUIResources() {
        configPanel.disposeUIResources();
        configPanel = null;
    }

    @Override
    public JavaDocSettings getConfiguration() {
        JavaDocSettings javaDocSettings = new JavaDocSettings();
        XmlSerializerUtil.copyBean(getSettings(), javaDocSettings);
        return javaDocSettings;
    }

    @Nullable
    @Override
    public Element getState() {
        Element root = new Element("JAVA_DOC_SETTINGS_PLUGIN");
        if (settings != null) {
            settings.addToDom(root);
        }
        return root;
    }

    @Override
    public void loadState(Element javaDocSettings) {
        settings = new JavaDocSettings(javaDocSettings);
        loadedStoredConfig = true;
    }

    private JavaDocSettings getSettings() {
        if (!loadedStoredConfig) {
            // setup default values
            settings = new JavaDocSettings();
            Set<Level> levels = new HashSet<Level>();
            levels.add(Level.TYPE);
            levels.add(Level.METHOD);
            levels.add(Level.FIELD);

            Set<Visibility> visibilities = new HashSet<Visibility>();
            visibilities.add(Visibility.PUBLIC);
            visibilities.add(Visibility.PROTECTED);

            settings.getGeneralSettings().setOverriddenMethods(Boolean.FALSE);
            settings.getGeneralSettings().setMode(Mode.UPDATE);
            settings.getGeneralSettings().setLevels(levels);
            settings.getGeneralSettings().setVisibilities(visibilities);
        }
        return settings;
    }

}
