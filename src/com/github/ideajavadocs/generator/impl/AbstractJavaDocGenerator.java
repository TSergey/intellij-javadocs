package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.template.DocTemplateManager;
import com.github.ideajavadocs.template.DocTemplateProcessor;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    private DocTemplateManager docTemplateManager;
    private DocTemplateProcessor docTemplateProcessor;
    private PsiElementFactory psiElementFactory;

    public AbstractJavaDocGenerator(@NotNull Project project) {
        docTemplateManager = ServiceManager.getService(project, DocTemplateManager.class);
        docTemplateProcessor = ServiceManager.getService(DocTemplateProcessor.class);
        psiElementFactory = PsiElementFactory.SERVICE.getInstance(project);
    }

    @Override
    @NotNull
    public final PsiDocComment generate(@NotNull T element, boolean replace) {
        PsiDocComment oldDocComment = null;
        PsiElement firstElement = element.getFirstChild();
        if (firstElement instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) firstElement;
        }

        JavaDoc newJavaDoc = generateJavaDoc(element);
        if (!replace && oldDocComment != null) {
            JavaDoc oldJavaDoc = JavaDocUtils.createJavaDoc(oldDocComment);
            newJavaDoc = JavaDocUtils.mergeJavaDocs(oldJavaDoc, newJavaDoc);
        }
        String javaDoc = newJavaDoc.toJavaDoc();
        return psiElementFactory.createDocCommentFromText(javaDoc);
    }

    @NotNull
    public DocTemplateManager getDocTemplateManager() {
        return docTemplateManager;
    }

    @NotNull
    public DocTemplateProcessor getDocTemplateProcessor() {
        return docTemplateProcessor;
    }

    @NotNull
    public PsiElementFactory getPsiElementFactory() {
        return psiElementFactory;
    }

    @NotNull
    protected abstract JavaDoc generateJavaDoc(@NotNull T element);

}
