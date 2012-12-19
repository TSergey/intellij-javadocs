package com.github.ideajavadocs.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;

import org.jetbrains.annotations.NotNull;

public interface JavaDocWriter {

    String WRITE_JAVADOC_COMMAND_NAME = "com.github.ideajavadocs.operation.JavaDocWriter";
    String WRITE_JAVADOC_COMMAND_GROUP = "com.github.ideajavadocs.operation";

    void write(@NotNull PsiDocComment javaDoc, @NotNull PsiElement beforeElement);

}
