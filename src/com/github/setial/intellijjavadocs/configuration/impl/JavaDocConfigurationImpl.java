package com.github.setial.intellijjavadocs.configuration.impl;

import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.github.setial.intellijjavadocs.template.DocTemplateManager;
import com.github.setial.intellijjavadocs.ui.settings.ConfigPanel;
import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
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

    private Project project;
    private JavaDocSettings settings;
    private ConfigPanel configPanel;
    private DocTemplateManager templateManager;
    private boolean loadedStoredConfig = false;

    public JavaDocConfigurationImpl(Project project) {
        this.project = project;
        templateManager = ServiceManager.getService(project, DocTemplateManager.class);
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
            configPanel = new ConfigPanel(getSettings());
        }
        reset();
        return configPanel;
    }

    @Override
    public boolean isModified() {
        return configPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configPanel.apply();
        templateManager.setClassTemplates(settings.getTemplateSettings().getClassTemplates());
        templateManager.setConstructorTemplates(settings.getTemplateSettings().getConstructorTemplates());
        templateManager.setMethodTemplates(settings.getTemplateSettings().getMethodTemplates());
        templateManager.setFieldTemplates(settings.getTemplateSettings().getFieldTemplates());
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
        JavaDocSettings result;
        try {
            result = (JavaDocSettings) getSettings().clone();
        } catch (Exception e) {
            // return null if cannot clone object
            result = null;
        }
        return result;
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

            settings.getTemplateSettings().setClassTemplates(templateManager.getClassTemplates());
            settings.getTemplateSettings().setConstructorTemplates(templateManager.getConstructorTemplates());
            settings.getTemplateSettings().setMethodTemplates(templateManager.getMethodTemplates());
            settings.getTemplateSettings().setFieldTemplates(templateManager.getFieldTemplates());
        }
        return settings;
    }

}
