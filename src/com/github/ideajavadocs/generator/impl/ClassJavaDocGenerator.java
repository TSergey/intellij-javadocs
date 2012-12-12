package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;

public class ClassJavaDocGenerator implements JavaDocGenerator<PsiClass> {

    @Override
    public PsiDocComment generate(PsiClass element, boolean replace) {
        PsiDocComment oldDocComment = null;
        if (element.getFirstChild() instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) element.getFirstChild();
        }
        if (oldDocComment != null) {

            // TODO merge or replace javadoc
        }

        String name = element.getName();

        StringBuilder result = new StringBuilder();
        result
                .append("/**\n")
                .append("* The class of type ")
                .append(name)
                .append(".\n")
                .append("*/");

        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        PsiDocComment docElement = psiElementFactory.createDocCommentFromText(result.toString());

        return docElement;
    }

}
