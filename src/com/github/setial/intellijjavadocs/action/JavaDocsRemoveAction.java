package com.github.setial.intellijjavadocs.action;

import com.github.setial.intellijjavadocs.operation.JavaDocWriter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * The type Java docs remove action.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocsRemoveAction extends JavaDocsGenerateAction {

    private JavaDocWriter writer;

    /**
     * Instantiates a new Java docs remove action.
     */
    public JavaDocsRemoveAction() {
        writer = ApplicationManager.getApplication().getComponent(JavaDocWriter.class);
    }

    @Override
    protected void processElement(@NotNull PsiElement element) {
        writer.remove(element);
    }
}
