package com.github.ideajavadocs.operation.impl;

import com.github.ideajavadocs.operation.JavaDocWriter;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.ReadonlyStatusHandler.OperationStatus;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;

import java.util.Arrays;

public class JavaDocWriterImpl implements JavaDocWriter {

    @Override
    public void write(final PsiDocComment javaDoc, final PsiElement beforeElement) {
        OperationStatus status = ReadonlyStatusHandler.getInstance(beforeElement.getProject()).
                ensureFilesWritable(Arrays.asList(beforeElement.getContainingFile().getVirtualFile()));
        if (status.hasReadonlyFiles()) {
            // TODO show error message
            // TODO stop execution
        }

        WriteCommandAction command = new WriteCommandActionImpl(javaDoc, beforeElement);
        RunResult result = command.execute();

        // TODO check result and show warning if there was some errors
    }

    private static class WriteCommandActionImpl extends WriteCommandAction {

        private PsiDocComment javaDoc;
        private PsiElement element;

        public WriteCommandActionImpl(PsiDocComment javaDoc, PsiElement element) {
            super(
                    element.getProject(),
                    WRITE_JAVADOC_COMMAND_NAME,
                    WRITE_JAVADOC_COMMAND_GROUP,
                    element.getContainingFile());
            this.javaDoc = javaDoc;
            this.element = element;
        }

        @Override
        protected void run(Result result) throws Throwable {
            if (javaDoc == null) {
                // TODO create result object
                return;
            }
            if (element.getFirstChild() instanceof PsiDocComment) {
                element.getFirstChild().replace(javaDoc);
            } else {
                element.addBefore(javaDoc, element.getFirstChild());
            }
            CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(element.getProject());
            // TODO figure out how to reformat javadoc only
            codeStyleManager.reformat(element);
        }
    }
}
