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

/**
 * The type Abstract java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {

    private DocTemplateManager docTemplateManager;
    private DocTemplateProcessor docTemplateProcessor;
    private PsiElementFactory psiElementFactory;

    /**
     * Instantiates a new Abstract java doc generator.
     *
     * @param project the Project
     */
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

    /**
     * Gets the doc template manager.
     *
     * @return the Doc template manager
     */
    @NotNull
    public DocTemplateManager getDocTemplateManager() {
        return docTemplateManager;
    }

    /**
     * Gets the doc template processor.
     *
     * @return the Doc template processor
     */
    @NotNull
    public DocTemplateProcessor getDocTemplateProcessor() {
        return docTemplateProcessor;
    }

    /**
     * Gets the psi element factory.
     *
     * @return the Psi element factory
     */
    @NotNull
    public PsiElementFactory getPsiElementFactory() {
        return psiElementFactory;
    }

    /**
     * Generate java doc.
     *
     * @param element the Element
     * @return the Java doc
     */
    @NotNull
    protected abstract JavaDoc generateJavaDoc(@NotNull T element);

}
