package com.github.ideajavadocs.transformation;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocTag;
import com.intellij.psi.javadoc.PsiDocComment;

import java.util.List;
import java.util.Map;

public class JavaDocConverter {

    private PsiDocComment docComment;

    public JavaDocConverter(PsiDocComment docComment) {
        this.docComment = docComment;
    }

    // PsiWhiteSpace : \n
    // oldDocComment.getDescriptionElements()
    public List<String> convertDescription() {
        // TODO
        return null;
    }

    // oldDocComment.getTags()[0].getName()
    // oldDocComment.getTags()[0].getDataElements();
    public Map<String, JavaDocTag> convertTags() {
        // TODO
        return null;
    }

    public JavaDoc convertJavaDoc() {
        return new JavaDoc(convertDescription(), convertTags());
    }

}
