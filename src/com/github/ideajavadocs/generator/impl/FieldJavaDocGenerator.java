package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;

public class FieldJavaDocGenerator implements JavaDocGenerator<PsiField> {

    @Override
    public PsiDocComment generate(PsiField element, boolean replace) {
        PsiDocComment oldDocComment = null;
        if (element.getFirstChild() instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) element.getFirstChild();
        }
        if (oldDocComment != null) {

            // TODO merge or replace javadoc
        }

        String name = element.getName();
        PsiType type = element.getType();

        StringBuilder result = new StringBuilder();
        result
                .append("/** The ")
                .append(name)
                .append(" field ")
                .append("of type: ")
                .append(type.getCanonicalText())
                .append("*/");

        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        PsiDocComment docElement = psiElementFactory.createDocCommentFromText(result.toString());

        return docElement;
    }

}
