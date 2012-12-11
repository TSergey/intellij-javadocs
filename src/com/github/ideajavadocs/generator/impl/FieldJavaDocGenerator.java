package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;

public class FieldJavaDocGenerator implements JavaDocGenerator<PsiClass> {

    @Override
    public PsiDocComment generate(PsiClass element, boolean replace) {
        String javaDoc =
                "/**\n" +
                "\t *\n" +
                "\t */";
        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        PsiDocComment docElement = psiElementFactory.createDocCommentFromText(javaDoc);

        return docElement;
    }

}
