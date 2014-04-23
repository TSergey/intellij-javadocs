package com.github.setial.intellijjavadocs.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Java docs generate action.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocsGenerateAction extends JavaDocGenerateAction {

    /**
     * Instantiates a new Java docs generate action.
     */
    public JavaDocsGenerateAction() {
        super(new JavaDocsGenerateHandler());
    }

    /**
     * Action performed.
     *
     * @param e the Event
     */
    public void actionPerformed(AnActionEvent e) {
        final PsiFile file = DataKeys.PSI_FILE.getData(e.getDataContext());
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

    /**
     * Gets the classes.
     *
     * @param element the Element
     * @return the Classes
     */
    private List<PsiClass> getClasses(PsiElement element) {
        List<PsiClass> elements = new LinkedList<PsiClass>();
        List<PsiClass> classElements = PsiTreeUtil.getChildrenOfTypeAsList(element, PsiClass.class);
        elements.addAll(classElements);
        for (PsiClass classElement : classElements) {
            elements.addAll(getClasses(classElement));
        }
        return elements;
    }

}
