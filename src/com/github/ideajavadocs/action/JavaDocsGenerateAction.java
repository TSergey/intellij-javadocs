package com.github.ideajavadocs.action;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.generator.impl.ClassJavaDocGenerator;
import com.github.ideajavadocs.generator.impl.FieldJavaDocGenerator;
import com.github.ideajavadocs.generator.impl.MethodJavaDocGenerator;
import com.github.ideajavadocs.operation.JavaDocWriter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class JavaDocsGenerateAction extends AnAction {

    private JavaDocWriter writer;

    public JavaDocsGenerateAction() {
        writer = ServiceManager.getService(JavaDocWriter.class);
    }

    public void actionPerformed(AnActionEvent e) {
        PsiFile file = DataKeys.PSI_FILE.getData(e.getDataContext());
        if (file == null) {
            // TODO show message
            return;
        }
        List<PsiElement> elements = new LinkedList<PsiElement>();
        // Find all class elements
        List<PsiClass> classElements = getClasses(file);
        elements.addAll(classElements);
        for (PsiClass classElement : classElements) {
            elements.addAll(PsiTreeUtil.getChildrenOfTypeAsList(classElement, PsiMethod.class));
            elements.addAll(PsiTreeUtil.getChildrenOfTypeAsList(classElement, PsiField.class));
        }
        for (PsiElement element : elements) {
            processElement(element);
        }
    }

    private List<PsiClass> getClasses(PsiElement element) {
        List<PsiClass> elements = new LinkedList<PsiClass>();
        List<PsiClass> classElements = PsiTreeUtil.getChildrenOfTypeAsList(element, PsiClass.class);
        elements.addAll(classElements);
        for (PsiClass classElement : classElements) {
            elements.addAll(getClasses(classElement));
        }
        return elements;
    }

    @Nullable
    private JavaDocGenerator getGenerator(@NotNull PsiElement element) {
        Project project = element.getProject();
        JavaDocGenerator generator = null;
        if (PsiClass.class.isAssignableFrom(element.getClass())) {
            generator = new ClassJavaDocGenerator(project);
        } else if (PsiMethod.class.isAssignableFrom(element.getClass())) {
            generator = new MethodJavaDocGenerator(project);
        } else if (PsiField.class.isAssignableFrom(element.getClass())) {
            generator = new FieldJavaDocGenerator(project);
        }
        return generator;
    }

    private void processElement(@NotNull PsiElement element) {
        JavaDocGenerator generator = getGenerator(element);
        if (generator != null) {
            @SuppressWarnings("unchecked")
            PsiDocComment javaDoc = generator.generate(element, false);
            writer.write(javaDoc, element);
        }
    }

}
