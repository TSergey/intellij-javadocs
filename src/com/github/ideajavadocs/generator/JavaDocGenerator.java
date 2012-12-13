package com.github.ideajavadocs.generator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

public interface JavaDocGenerator<T extends PsiElement> {

    @NotNull
    PsiDocComment generate(@NotNull T element, boolean replace);

}
