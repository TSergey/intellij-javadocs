package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;

public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    @Override
    protected JavaDoc generate(PsiField element) {
        // TODO implement


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

        return null;
    }
}
