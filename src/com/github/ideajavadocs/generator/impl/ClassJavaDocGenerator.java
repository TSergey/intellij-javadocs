package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;

public class ClassJavaDocGenerator extends AbstractJavaDocGenerator<PsiClass> {

    @Override
    public PsiDocComment generate(PsiClass element, boolean replace) {
        String name = element.getName();

        StringBuilder result = new StringBuilder();
        result
                .append("/**\n")
                .append("* The class of type ")
                .append(name)
                .append(".\n")
                .append("*/");

        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        return psiElementFactory.createDocCommentFromText(result.toString());
    }

    @Override
    protected JavaDoc generate(PsiClass element) {
        // TODO implement
        return null;
    }

}
