package com.github.setial.intellijjavadocs.model.settings;

import com.github.setial.intellijjavadocs.utils.XmlUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Element;

import java.io.Serializable;

/**
 * The type Java doc settings.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocSettings implements Serializable {

    private static JavaDocSettings instance = null;
    private static final String MODE = "MODE";
    private static final String LEVELS = "LEVELS";
    private static final String LEVEL = "LEVEL";
    private static final String VISIBILITIES = "VISIBILITIES";
    private static final String VISIBILITY = "VISIBILITY";
    private static final String OVERRIDDEN_METHODS = "OVERRIDDEN_METHODS";
    private static final String SPLITTED_CLASS_NAME = "SPLITTED_CLASS_NAME";
    private static final String GENERAL = "GENERAL";
    private static final String TEMPLATES = "TEMPLATES";
    private static final String CLASS = "CLASS";
    private static final String CLASSES = "CLASSES";
    private static final String CONSTRUCTOR = "CONSTRUCTOR";
    private static final String CONSTRUCTORS = "CONSTRUCTORS";
    private static final String FIELD = "FIELD";
    private static final String FIELDS = "FIELDS";
    private static final String METHOD = "METHOD";
    private static final String METHODS = "METHODS";
    public static boolean loadedStoredConfig = false;

    private GeneralSettings generalSettings = new GeneralSettings();
    private TemplateSettings templateSettings = new TemplateSettings();

    public static JavaDocSettings getInstance(Element element) {
        if (instance == null) {
            instance = new JavaDocSettings(element);
        }
        return instance;
    }

    public static JavaDocSettings getInstance() {
        return instance;
    }

    public void setInstance(JavaDocSettings instance) {
        this.instance = instance;
    }

    /**
     * Instantiates a new Java doc settings object.
     */
    public JavaDocSettings() {
    }

    /**
     * Instantiates a new Java doc settings.
     *
     * @param element the element
     */
    private JavaDocSettings(Element element) {
        Element general = element.getChild(GENERAL);
        if (general != null) {
            generalSettings.setMode(XmlUtils.getValue(general, MODE, Mode.class));
            generalSettings.setOverriddenMethods(Boolean.parseBoolean(general.getChild(OVERRIDDEN_METHODS).getValue()));
            generalSettings.setSplittedClassName(Boolean.parseBoolean(general.getChild(SPLITTED_CLASS_NAME).getValue()));
            generalSettings.setLevels(XmlUtils.getValues(general, LEVELS, LEVEL, Level.class));
            generalSettings.setVisibilities(XmlUtils.getValues(general, VISIBILITIES, VISIBILITY, Visibility.class));
        }
        Element templates = element.getChild(TEMPLATES);
        if (templates != null) {
            templateSettings.setClassTemplates(XmlUtils.getMap(templates, CLASSES, CLASS));
            templateSettings.setConstructorTemplates(XmlUtils.getMap(templates, CONSTRUCTORS, CONSTRUCTOR));
            templateSettings.setFieldTemplates(XmlUtils.getMap(templates, FIELDS, FIELD));
            templateSettings.setMethodTemplates(XmlUtils.getMap(templates, METHODS, METHOD));
        }
    }

    /**
     * Create dom model from this object and add it to root element passed as parameter.
     *
     * @param root the root
     */
    public void addToDom(Element root) {
        Element general = new Element(GENERAL);
        root.addContent(general);
        if (generalSettings.getMode() != null) {
            general.addContent(XmlUtils.getElement(MODE, generalSettings.getMode().toString()));
            general.addContent(XmlUtils.getElement(OVERRIDDEN_METHODS, String.valueOf(generalSettings.isOverriddenMethods())));
            general.addContent(XmlUtils.getElement(SPLITTED_CLASS_NAME, String.valueOf(generalSettings.isSplittedClassName())));
            general.addContent(XmlUtils.getElement(LEVELS, LEVEL, generalSettings.getLevels()));
            general.addContent(XmlUtils.getElement(VISIBILITIES, VISIBILITY, generalSettings.getVisibilities()));
        }

        Element templates = new Element(TEMPLATES);
        root.addContent(templates);
        templates.addContent(XmlUtils.getElement(CLASSES, CLASS, templateSettings.getClassTemplates()));
        templates.addContent(XmlUtils.getElement(CONSTRUCTORS, CONSTRUCTOR, templateSettings.getConstructorTemplates()));
        templates.addContent(XmlUtils.getElement(METHODS, METHOD, templateSettings.getMethodTemplates()));
        templates.addContent(XmlUtils.getElement(FIELDS, FIELD, templateSettings.getFieldTemplates()));
    }

    /**
     * Gets general settings.
     *
     * @return the general settings
     */
    public GeneralSettings getGeneralSettings() {
        return generalSettings;
    }

    /**
     * Gets template settings.
     *
     * @return the template settings
     */
    public TemplateSettings getTemplateSettings() {
        return templateSettings;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        JavaDocSettings result = new JavaDocSettings();
        try {
            if (this.generalSettings != null) {
                result.generalSettings = (GeneralSettings) BeanUtils.cloneBean(this.generalSettings);
            }
            if (this.templateSettings != null) {
                result.templateSettings = (TemplateSettings) BeanUtils.cloneBean(this.templateSettings);
            }
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
        return result;
    }

    public static void load() {
        instance = new JavaDocSettings();
    }
}
