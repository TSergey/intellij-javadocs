package com.github.setial.intellijjavadocs.operation;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.intellij.psi.PsiElement;
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
    void write(@NotNull JavaDoc javaDoc, @NotNull PsiElement beforeElement);

    /**
     * Remove void.
     *
     * @param beforeElement the before element
     */
    void remove(@NotNull PsiElement beforeElement);

}
