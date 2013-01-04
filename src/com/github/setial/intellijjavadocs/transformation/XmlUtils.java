package com.github.setial.intellijjavadocs.transformation;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringWriter;

public class XmlUtils {

    private static final XMLOutputter NORMALIZE_OUTPUTTER = new XMLOutputter();
    private static final XMLOutputter TRIM_OUTPUTTER = new XMLOutputter();
    static {
        Format normalizeFormat = Format.getRawFormat();
        normalizeFormat.setTextMode(Format.TextMode.NORMALIZE);
        NORMALIZE_OUTPUTTER.setFormat(normalizeFormat);

        Format trimFormat = Format.getRawFormat();
        trimFormat.setTextMode(Format.TextMode.TRIM);
        TRIM_OUTPUTTER.setFormat(trimFormat);
    }

    public static String trimElementContent(Element element) throws IOException {
        StringWriter writer = new StringWriter();
        TRIM_OUTPUTTER.outputElementContent(element, writer);
        return writer.toString();
    }

    public static String normalizeTemplate(String template) throws IOException {
        StringWriter writer = new StringWriter();
        Element element = new Element("template");
        element.addContent(template);
        NORMALIZE_OUTPUTTER.outputElementContent(element, writer);
        return writer.toString().replaceAll("\\\\n", "\n");
    }

    private XmlUtils() {}

}
