package com.github.setial.intellijjavadocs.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * The interface Java doc writer.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocWriter {

    /**
     * Write java doc.
     *
     * @param javaDoc       the Java doc
     * @param beforeElement the element to place javadoc before it
     */
    void write(@NotNull PsiDocComment javaDoc, @NotNull PsiElement beforeElement);

    /**
     * Remove void.
     *
     * @param beforeElement the before element
     */
    void remove(@NotNull PsiElement beforeElement);

}
