package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.intellij.psi.*;
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
        PsiModifierList modifiers = element.getModifierList();
        PsiType type = element.getType();

        PsiDocComment docElement = null;
        StringBuilder result = new StringBuilder();
        if (modifiers != null && modifiers.hasModifierProperty(PsiModifier.PUBLIC)) {
            result
                    .append("/** The ")
                    .append(name)
                    .append(" field ")
                    .append("of type: ")
                    .append(type.getCanonicalText())
                    .append("*/");

            PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
            docElement = psiElementFactory.createDocCommentFromText(result.toString());
        }


        return docElement;
    }

}
