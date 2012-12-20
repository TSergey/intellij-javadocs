package com.github.ideajavadocs.generator;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

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
     * @param replace the Replace flag
     * @return the Psi doc comment
     */
    @NotNull
    PsiDocComment generate(@NotNull T element, boolean replace);

}
