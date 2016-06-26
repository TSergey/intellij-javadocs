package com.github.setial.intellijjavadocs.configuration.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.exception.SetupTemplateException;
import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.github.setial.intellijjavadocs.template.DocTemplateManager;
import com.github.setial.intellijjavadocs.ui.settings.ConfigPanel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
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
                        value = StoragePathMacros.APP_CONFIG + "/intellij-javadocs.xml"
                )
        }
)
public class JavaDocConfigurationImpl implements JavaDocConfiguration, Configurable,
        PersistentStateComponent<Element> {

    private static final Logger LOGGER = Logger.getInstance(JavaDocConfigurationImpl.class);

    public ConfigPanel configPanel;
    public DocTemplateManager templateManager;

    /**
     * Instantiates a new Java doc configuration object.
     */
    public JavaDocConfigurationImpl() {
        templateManager = ApplicationManager.getApplication().getComponent(DocTemplateManager.class);
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
        return "JavaDoc";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
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
        setupTemplates();
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
        if (JavaDocSettings.getInstance() != null) {
            try{
                JavaDocSettings.getInstance().addToDom(root);
            }catch (Exception e){
                e.printStackTrace();
            }
            JavaDocSettings.getInstance().loadedStoredConfig = true;
        }
        return root;
    }

    @Override
    public void loadState(Element javaDocSettings) {
        JavaDocSettings.getInstance(javaDocSettings);
        setupTemplates();
        JavaDocSettings.getInstance().loadedStoredConfig = true;
    }

    private JavaDocSettings getSettings() {
        if (!JavaDocSettings.getInstance().loadedStoredConfig) {
            if (JavaDocSettings.getInstance() == null) {
                JavaDocSettings.load();
            }
            // setup default values
            Set<Level> levels = new HashSet<Level>();
            levels.add(Level.TYPE);
            levels.add(Level.METHOD);
            levels.add(Level.FIELD);

            Set<Visibility> visibilities = new HashSet<Visibility>();
            visibilities.add(Visibility.PUBLIC);
            visibilities.add(Visibility.PROTECTED);
            visibilities.add(Visibility.DEFAULT);

            JavaDocSettings.getInstance().getGeneralSettings().setOverriddenMethods(false);
            JavaDocSettings.getInstance().getGeneralSettings().setSplittedClassName(true);
            JavaDocSettings.getInstance().getGeneralSettings().setMode(Mode.UPDATE);
            JavaDocSettings.getInstance().getGeneralSettings().setLevels(levels);
            JavaDocSettings.getInstance().getGeneralSettings().setVisibilities(visibilities);

            JavaDocSettings.getInstance().getTemplateSettings().setClassTemplates(templateManager.getClassTemplates());
            JavaDocSettings.getInstance().getTemplateSettings().setConstructorTemplates(templateManager.getConstructorTemplates());
            JavaDocSettings.getInstance().getTemplateSettings().setMethodTemplates(templateManager.getMethodTemplates());
            JavaDocSettings.getInstance().getTemplateSettings().setFieldTemplates(templateManager.getFieldTemplates());
        }
        return JavaDocSettings.getInstance();
    }

    private void setupTemplates() {
        try {
            templateManager.setClassTemplates(JavaDocSettings.getInstance().getTemplateSettings().getClassTemplates());
            templateManager.setConstructorTemplates(JavaDocSettings.getInstance().getTemplateSettings().getConstructorTemplates());
            templateManager.setMethodTemplates(JavaDocSettings.getInstance().getTemplateSettings().getMethodTemplates());
            templateManager.setFieldTemplates(JavaDocSettings.getInstance().getTemplateSettings().getFieldTemplates());
        } catch (SetupTemplateException e) {
            LOGGER.error(e);
        }
    }

}
