package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

public class FieldJavaDocGenerator extends AbstractJavaDocGenerator<PsiField> {

    @Override
    @NotNull
    protected JavaDoc generate(@NotNull PsiField element) {
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
