package com.github.ideajavadocs.model.settings;

import org.jdom.Element;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The type Java doc settings.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocSettings implements Serializable {

    private static final String MODE = "MODE";
    private static final String LEVELS = "LEVELS";
    private static final String LEVEL = "LEVEL";
    private static final String VISIBILITIES = "VISIBILITIES";
    private static final String VISIBILITY = "VISIBILITY";
    private static final String OVERRIDDEN_METHODS = "OVERRIDDEN_METHODS";
    public static final String GENERAL = "GENERAL";

    private GeneralSettings generalSettings = new GeneralSettings();
    private TemplateSettings templateSettings = new TemplateSettings();
    public JavaDocSettings() {
    }

    public JavaDocSettings(Element element) {
        Element general = element.getChild(GENERAL);
        if (general != null) {
            generalSettings.setMode(getValue(general, MODE, Mode.class));
            generalSettings.setOverriddenMethods(Boolean.parseBoolean(general.getChild(OVERRIDDEN_METHODS).getValue()));
            generalSettings.setLevels(getValues(general, LEVELS, LEVEL, Level.class));
            generalSettings.setVisibilities(getValues(general, VISIBILITIES, VISIBILITY, Visibility.class));
        }
    }

    public void addToDom(Element root) {
        Element general = new Element(GENERAL);
        root.addContent(general);
        general.addContent(getElement(MODE, generalSettings.getMode().toString()));
        general.addContent(getElement(OVERRIDDEN_METHODS, generalSettings.isOverriddenMethods().toString()));
        general.addContent(getElement(LEVELS, LEVEL, generalSettings.getLevels()));
        general.addContent(getElement(VISIBILITIES, VISIBILITY, generalSettings.getVisibilities()));
    }

    private Element getElement(String name, String value) {
        Element element = new Element(name);
        element.addContent(value);
        return element;
    }

    private Element getElement(String nameParent, String nameChild, Collection<?> values) {
        Element root = new Element(nameParent);
        for (Object value : values) {
            root.addContent(getElement(nameChild, value.toString()));
        }
        return root;
    }

    private <T extends Enum<T>> T getValue(Element element, String name, Class<T> type) {
        T enumVal = null;
        Element child = element.getChild(name);
        if (child != null) {
            String result = child.getValue();
            if (result != null) {
                enumVal = Enum.valueOf(type, result);
            }
        }
        return enumVal;
    }

    private <T extends Enum<T>> Set<T> getValues(Element element, String parentName, String childName, Class<T> type) {
        Set<T> result = new LinkedHashSet<T>();
        Element root = element.getChild(parentName);
        for (Object value : root.getChildren(childName)) {
            if (value instanceof Element) {
                Element elem = (Element) value;
                String name = elem.getValue();
                result.add(Enum.valueOf(type, name));
            }
        }
        return result;
    }

    public GeneralSettings getGeneralSettings() {
        return generalSettings;
    }

    public TemplateSettings getTemplateSettings() {
        return templateSettings;
    }

}
