package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.transformation.JavaDocUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    @Override
    @NotNull
    public final PsiDocComment generate(@NotNull T element, boolean replace) {
        PsiDocComment oldDocComment = null;
        PsiElement firstElement = element.getFirstChild();
        if (firstElement instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) firstElement;
        }
        JavaDoc newJavaDoc = generate(element);
        if (!replace && oldDocComment != null) {
            JavaDoc oldJavaDoc = JavaDocUtil.createJavaDoc(oldDocComment);
            newJavaDoc = JavaDocUtil.mergeJavaDocs(oldJavaDoc, newJavaDoc);
        }
        String javaDoc = newJavaDoc.toJavaDoc();
        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        return psiElementFactory.createDocCommentFromText(javaDoc);
    }

    @NotNull
    protected abstract JavaDoc generate(@NotNull T element);

}
