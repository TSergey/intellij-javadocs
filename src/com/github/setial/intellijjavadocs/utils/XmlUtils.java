package com.github.setial.intellijjavadocs.utils;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class XmlUtils {

    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";

    public static String trimElementContent(Element element) throws IOException {
        return element.getTextTrim();
    }

    public static String normalizeTemplate(String template) throws IOException {
        Element element = new Element("template");
        element.addContent(template);
        return element.getText().replaceAll("\\\\n", "\n").replaceAll("\n\n", "\n");
    }

    public static Element getElement(String name, String value) {
        Element element = new Element(name);
        element.addContent(value);
        return element;
    }

    public static Element getElement(String nameParent, String nameChild, Collection<?> values) {
        Element root = new Element(nameParent);
        for (Object value : values) {
            root.addContent(getElement(nameChild, value.toString()));
        }
        return root;
    }

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

    private XmlUtils() {}

}
