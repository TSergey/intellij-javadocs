package com.github.setial.intellijjavadocs.operation;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * The interface Java doc writer.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocWriter extends ApplicationComponent {

    /**
     * The constant WRITE_JAVADOC_COMMAND_NAME.
     */
    String WRITE_JAVADOC_COMMAND_NAME = "JavaDocWriter";
    /**
     * The constant WRITE_JAVADOC_COMMAND_GROUP.
     */
    String WRITE_JAVADOC_COMMAND_GROUP = "com.github.setial.operation";

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
