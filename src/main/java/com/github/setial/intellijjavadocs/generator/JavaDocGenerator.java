package com.github.setial.intellijjavadocs.generator;

import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc generator.
 *
 * @param <T> the type parameter
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
    JavaDoc generate(@NotNull T element);

}
