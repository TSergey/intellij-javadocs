package com.github.ideajavadocs.model;

import com.intellij.psi.javadoc.PsiDocTag;

import java.util.List;

// oldDocComment.getTags()[12].getDataElements()
public class JavaDocTag {

    // Priority :0
    // PsiDocParamRef
    // PsiDocMethodOrFieldRef
    private String refParam;
    // Priority :1
    // PsiDocTagValue
    private String value;
    // Priority :2
    // PsiDocToken, each token is a new line string
    private List<String> description;

    public JavaDocTag(PsiDocTag docTag) {
        // TODO setup fields from docTag

    }

    public JavaDocTag(String refParam, String value, List<String> description) {
        this.refParam = refParam;
        this.value = value;
        this.description = description;
    }

    public String getRefParam() {
        return refParam;
    }

    public String getValue() {
        return value;
    }

    public List<String> getDescription() {
        return description;
    }

}
