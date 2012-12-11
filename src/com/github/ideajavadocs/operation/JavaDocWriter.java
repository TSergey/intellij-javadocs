package com.github.ideajavadocs.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;

public interface JavaDocWriter {

    void write(PsiDocComment javaDoc, PsiElement beforeElement);

}
