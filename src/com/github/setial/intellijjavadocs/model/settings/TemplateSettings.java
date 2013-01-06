package com.github.setial.intellijjavadocs.model.settings;

import org.apache.velocity.Template;

import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateSettings {

    private Map<String, String> classTemplates = new LinkedHashMap<String, String>();
    private Map<String, String> fieldTemplates = new LinkedHashMap<String, String>();
    private Map<String, String> methodTemplates = new LinkedHashMap<String, String>();
    private Map<String, String> constructorTemplates = new LinkedHashMap<String, String>();

    public Map<String, String> getClassTemplates() {
        return classTemplates;
    }

    public void setClassTemplates(Map<String, String> classTemplates) {
        this.classTemplates = classTemplates;
    }

    public Map<String, String> getConstructorTemplates() {
        return constructorTemplates;
    }

    public void setConstructorTemplates(Map<String, String> constructorTemplates) {
        this.constructorTemplates = constructorTemplates;
    }

    public Map<String, String> getFieldTemplates() {
        return fieldTemplates;
    }

    public void setFieldTemplates(Map<String, String> fieldTemplates) {
        this.fieldTemplates = fieldTemplates;
    }

    public Map<String, String> getMethodTemplates() {
        return methodTemplates;
    }

    public void setMethodTemplates(Map<String, String> methodTemplates) {
        this.methodTemplates = methodTemplates;
    }

}
