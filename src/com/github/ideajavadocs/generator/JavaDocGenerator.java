package com.github.ideajavadocs.generator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;

public interface JavaDocGenerator<T extends PsiElement> {

    PsiDocComment generate(T element, boolean replace);

}
