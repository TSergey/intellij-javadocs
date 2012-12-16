package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.template.TemplateManager;
import com.github.ideajavadocs.template.TemplateProcessor;
import com.github.ideajavadocs.transformation.JavaDocUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    private TemplateManager templateManager;
    private TemplateProcessor templateProcessor;

    public AbstractJavaDocGenerator(Project project) {
        templateManager = ServiceManager.getService(project, TemplateManager.class);
        templateProcessor = ServiceManager.getService(TemplateProcessor.class);
    }

    @Override
    @NotNull
    public final PsiDocComment generate(@NotNull T element, boolean replace) {
        PsiDocComment oldDocComment = null;
        PsiElement firstElement = element.getFirstChild();
        if (firstElement instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) firstElement;
        }
        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());

        String javaDocText = getTemplateProcessor().process(getTemplate(element), getParams(element));
        PsiDocComment javaDocComment = psiElementFactory.createDocCommentFromText(javaDocText);
        JavaDoc newJavaDoc = JavaDocUtil.createJavaDoc(javaDocComment);
        newJavaDoc = enrichJavaDoc(newJavaDoc);
        if (!replace && oldDocComment != null) {
            JavaDoc oldJavaDoc = JavaDocUtil.createJavaDoc(oldDocComment);
            newJavaDoc = JavaDocUtil.mergeJavaDocs(oldJavaDoc, newJavaDoc);
        }
        String javaDoc = newJavaDoc.toJavaDoc();
        return psiElementFactory.createDocCommentFromText(javaDoc);
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public TemplateProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    @NotNull
    protected abstract String getTemplate(@NotNull T element);

    @NotNull
    protected abstract Map<String, String> getParams(@NotNull T element);

    protected abstract JavaDoc enrichJavaDoc(@NotNull JavaDoc newJavaDoc);

}
