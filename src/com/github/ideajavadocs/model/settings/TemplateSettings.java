package com.github.ideajavadocs.model.settings;

import org.apache.velocity.Template;

import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateSettings {

    private Map<String, Template> classTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> fieldTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> methodTemplates = new LinkedHashMap<String, Template>();
    private Map<String, Template> constructorTemplates = new LinkedHashMap<String, Template>();

    public Map<String, Template> getClassTemplates() {
        return classTemplates;
    }

    public void setClassTemplates(Map<String, Template> classTemplates) {
        this.classTemplates = classTemplates;
    }

    public Map<String, Template> getConstructorTemplates() {
        return constructorTemplates;
    }

    public void setConstructorTemplates(Map<String, Template> constructorTemplates) {
        this.constructorTemplates = constructorTemplates;
    }

    public Map<String, Template> getFieldTemplates() {
        return fieldTemplates;
    }

    public void setFieldTemplates(Map<String, Template> fieldTemplates) {
        this.fieldTemplates = fieldTemplates;
    }

    public Map<String, Template> getMethodTemplates() {
        return methodTemplates;
    }

    public void setMethodTemplates(Map<String, Template> methodTemplates) {
        this.methodTemplates = methodTemplates;
    }

}
