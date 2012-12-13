package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.model.JavaDoc;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;

public abstract class AbstractJavaDocGenerator implements JavaDocGenerator {

    @Override
    public PsiDocComment generate(PsiElement element, boolean replace) {
        PsiDocComment oldDocComment = null;
        if (element.getFirstChild() instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) element.getFirstChild();
        }
        JavaDoc newJavaDoc = generate(element);
        if (!replace) {
            JavaDoc oldJavaDoc = new JavaDoc(oldDocComment);
            newJavaDoc = mergeJavaDocs(oldJavaDoc, newJavaDoc);
        }
        // TODO convert newJavaDoc to string representation
        String javaDoc = "";
        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        return psiElementFactory.createDocCommentFromText(javaDoc);
    }

    protected abstract JavaDoc generate(PsiElement element);

    private JavaDoc mergeJavaDocs(JavaDoc oldJavaDoc, JavaDoc newJavaDoc) {
        // TODO implement code to merge javadocs

        return oldJavaDoc;
    }

}
