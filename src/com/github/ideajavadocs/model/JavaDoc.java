package com.github.ideajavadocs.model;

import com.intellij.psi.javadoc.PsiDocComment;

import java.util.List;
import java.util.Map;

public class JavaDoc {

    // PsiWhiteSpace : \n
    // oldDocComment.getDescriptionElements()
    private List<String> description;

    // oldDocComment.getTags()[0].getName()
    // oldDocComment.getTags()[0].getDataElements();
    private Map<String, JavaDocTag> tags;

    public JavaDoc(PsiDocComment docComment) {
        // TODO setup fields from docComment
    }

    public JavaDoc(List<String> description, Map<String, JavaDocTag> tags) {
        super();
        this.description = description;
        this.tags = tags;
    }

    public List<String> getDescription() {
        return description;
    }

    public Map<String, JavaDocTag> getTags() {
        return tags;
    }

}
