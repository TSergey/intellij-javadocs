package com.github.setial.intellijjavadocs.action;

import com.github.setial.intellijjavadocs.operation.JavaDocWriter;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * The type Java doc remove action.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocRemoveAction extends JavaDocGenerateAction {

    private JavaDocWriter writer;

    /**
     * Instantiates a new Java doc remove action.
     */
    public JavaDocRemoveAction() {
        this(new JavaDocHandler());
        writer = ApplicationManager.getApplication().getComponent(JavaDocWriter.class);
    }

    /**
     * Instantiates a new Java doc remove action.
     *
     * @param handler the handler
     */
    public JavaDocRemoveAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected void processElement(@NotNull PsiElement element) {
        writer.remove(element);
    }
}
