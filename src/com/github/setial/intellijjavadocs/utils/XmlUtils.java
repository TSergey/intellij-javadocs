package com.github.setial.intellijjavadocs.utils;

import org.jdom.Element;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The type Xml utils.
 *
 * @author Sergey Timofiychuk
 */
public class XmlUtils {

    /**
     * The constant KEY.
     */
    public static final String KEY = "KEY";

    /**
     * The constant VALUE.
     */
    public static final String VALUE = "VALUE";

    /**
     * Trim element content.
     *
     * @param element the element
     * @return the string
     * @throws IOException the iO exception
     */
    public static String trimElementContent(Element element) throws IOException {
        return element.getTextTrim();
    }

    /**
     * Normalize template.
     *
     * @param template the template
     * @return the string
     * @throws IOException the iO exception
     */
    public static String normalizeTemplate(String template) throws IOException {
        Element element = new Element("template");
        element.addContent(template);
        return element.getTextNormalize().replaceAll("\\\\n", "\n");
    }

    /**
     * Gets element.
     *
     * @param name  the name
     * @param value the value
     * @return the element
     */
    public static Element getElement(String name, String value) {
        Element element = new Element(name);
        element.addContent(value);
        return element;
    }

    /**
     * Gets element.
     *
     * @param nameParent the name parent
     * @param nameChild  the name child
     * @param values     the values
     * @return the element
     */
    public static Element getElement(String nameParent, String nameChild, Collection<?> values) {
        Element root = new Element(nameParent);
        for (Object value : values) {
            root.addContent(getElement(nameChild, value.toString()));
        }
        return root;
    }

    /**
     * Gets element.
     *
     * @param nameParent the name parent
     * @param nameChild  the name child
     * @param values     the values
     * @return the element
     */
    public static Element getElement(String nameParent, String nameChild, Map<String, String> values) {
        Element root = new Element(nameParent);
        for (Entry<String, String> entry : values.entrySet()) {
            Element child = new Element(nameChild);
            root.addContent(child);
            child.addContent(getElement(KEY, entry.getKey()));
            child.addContent(getElement(VALUE, entry.getValue()));
        }
        return root;
    }

    /**
     * Gets value.
     *
     * @param <T>     the type parameter
     * @param element the element
     * @param name    the name
     * @param type    the type
     * @return the value
     */
    public static <T extends Enum<T>> T getValue(Element element, String name, Class<T> type) {
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

    /**
     * Gets values.
     *
     * @param <T>        the type parameter
     * @param element    the element
     * @param parentName the parent name
     * @param childName  the child name
     * @param type       the type
     * @return the values
     */
    public static <T extends Enum<T>> Set<T> getValues(Element element, String parentName, String childName, Class<T> type) {
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

    /**
     * Gets values.
     *
     * @param element    the element
     * @param parentName the parent name
     * @param childName  the child name
     * @return the values
     */
    public static Set<Element> getValues(Element element, String parentName, String childName) {
        Set<Element> result = new LinkedHashSet<Element>();
        Element root = element.getChild(parentName);
        for (Object value : root.getChildren(childName)) {
            if (value instanceof Element) {
                result.add((Element) value);
            }
        }
        return result;
    }

    /**
     * Gets map.
     *
     * @param root       the root
     * @param parentName the parent name
     * @param childName  the child name
     * @return the map
     */
    public static Map<String, String> getMap(Element root, String parentName, String childName) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        Set<Element> templates = getValues(root, parentName, childName);
        for (Element template : templates) {
            String key = template.getChild(KEY).getValue();
            String value = template.getChild(VALUE).getValue();
            result.put(key, value);
        }
        return result;
    }

    private XmlUtils() {
    }

}
