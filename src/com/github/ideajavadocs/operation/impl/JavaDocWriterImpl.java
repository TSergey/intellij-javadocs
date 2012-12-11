package com.github.ideajavadocs.operation.impl;

import com.github.ideajavadocs.operation.JavaDocWriter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;

public class JavaDocWriterImpl implements JavaDocWriter {

    @Override
    public void write(final PsiDocComment javaDoc, final PsiElement beforeElement) {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {

            @Override
            public void run() {
                if (beforeElement.getFirstChild() instanceof PsiDocComment) {
                    beforeElement.getFirstChild().replace(javaDoc);
                } else {
                    beforeElement.addBefore(javaDoc, beforeElement.getFirstChild());
                }
                CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(beforeElement.getProject());
                // TODO figure out how to reformat javadoc only
                codeStyleManager.reformat(beforeElement);
            }

        });
    }

}
