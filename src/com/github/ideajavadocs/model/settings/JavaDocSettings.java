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

    private Mode mode;
    private Set<Level> levels;
    private Set<Visibility> visibilities;
    private boolean overriddenMethods;

    public JavaDocSettings() {
    }

    public JavaDocSettings(Element element) {
        setMode(getValue(element, MODE, Mode.class));
        setOverriddenMethods(Boolean.parseBoolean(element.getChild(OVERRIDDEN_METHODS).getValue()));
        setLevels(getValues(element, LEVELS, LEVEL, Level.class));
        setVisibilities(getValues(element, VISIBILITIES, VISIBILITY, Visibility.class));
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Set<Level> getLevels() {
        return levels;
    }

    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }

    public Set<Visibility> getVisibilities() {
        return visibilities;
    }

    public void setVisibilities(Set<Visibility> visibilities) {
        this.visibilities = visibilities;
    }

    public Boolean isOverriddenMethods() {
        return overriddenMethods;
    }

    public void setOverriddenMethods(Boolean overriddenMethods) {
        this.overriddenMethods = overriddenMethods;
    }

    public void addToDom(Element root) {
        root.addContent(getElement(MODE, getMode().toString()));
        root.addContent(getElement(OVERRIDDEN_METHODS, isOverriddenMethods().toString()));
        root.addContent(getElement(LEVELS, LEVEL, getLevels()));
        root.addContent(getElement(VISIBILITIES, VISIBILITY, getVisibilities()));
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

}
