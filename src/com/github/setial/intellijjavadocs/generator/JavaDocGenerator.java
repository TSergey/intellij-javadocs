package com.github.setial.intellijjavadocs.generator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocGenerator<T extends PsiElement> {

    /**
     * Generate java docs.
     *
     * @param element the Element
     * @return the Psi doc comment
     */
    @Nullable
    PsiDocComment generate(@NotNull T element);

}
