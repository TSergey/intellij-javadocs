package com.github.ideajavadocs.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;

public interface JavaDocWriter {

    String WRITE_JAVADOC_COMMAND_NAME = "com.github.ideajavadocs.operation.JavaDocWriter";
    String WRITE_JAVADOC_COMMAND_GROUP = "com.github.ideajavadocs.operation";

    void write(PsiDocComment javaDoc, PsiElement beforeElement);

}
