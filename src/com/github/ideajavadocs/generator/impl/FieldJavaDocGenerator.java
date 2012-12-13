package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;

public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    @Override
    public PsiDocComment generate(PsiField element, boolean replace) {
        String name = element.getName();
        PsiType type = element.getType();

        StringBuilder result = new StringBuilder();
        result
                .append("/** The ")
                .append(name)
                .append(" field ")
                .append("of type: ")
                .append(type.getCanonicalText())
                .append(".\n")
                .append("*/");

        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        return psiElementFactory.createDocCommentFromText(result.toString());
    }

    @Override
    protected JavaDoc generate(PsiField element) {
        // TODO implement
        return null;
    }
}
