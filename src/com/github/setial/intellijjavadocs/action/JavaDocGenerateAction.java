package com.github.setial.intellijjavadocs.action;

import com.github.setial.intellijjavadocs.generator.JavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.ClassJavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.FieldJavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.MethodJavaDocGenerator;
import com.github.setial.intellijjavadocs.operation.JavaDocWriter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Java doc generate action.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocGenerateAction extends AnAction {

    private JavaDocWriter writer;

    /**
     * Instantiates a new Java doc generate action.
     */
    public JavaDocGenerateAction() {
        writer = ServiceManager.getService(JavaDocWriter.class);
    }

    /**
     * Action performed.
     *
     * @param e the Event
     */
    public void actionPerformed(AnActionEvent e) {
        Editor editor = DataKeys.EDITOR.getData(e.getDataContext());
        if (editor == null) {
            // TODO show message
            return;
        }
        int startPosition = editor.getSelectionModel().getSelectionStart();
        int endPosition = editor.getSelectionModel().getSelectionEnd();
        PsiFile file = DataKeys.PSI_FILE.getData(e.getDataContext());
        if (file == null) {
            // TODO show message
            return;
        }
        List<PsiElement> elements = new LinkedList<PsiElement>();
        PsiElement firstElement = getJavaElement(PsiUtilCore.getElementAtOffset(file, startPosition));
        if (firstElement != null) {
            PsiElement element = firstElement;
            do {
                if (isAllowedElementType(element)) {
                    elements.add(element);
                }
                element = element.getNextSibling();
            } while (isElementInSelection(element, startPosition, endPosition));
        }
        for (PsiElement element : elements) {
            processElement(element);
        }
    }

    /**
     * Process element.
     *
     * @param element the Element
     */
    protected void processElement(@NotNull PsiElement element) {
        JavaDocGenerator generator = getGenerator(element);
        if (generator != null) {
            @SuppressWarnings("unchecked")
            PsiDocComment javaDoc = generator.generate(element);
            if (javaDoc != null) {
                writer.write(javaDoc, element);
            }
        }
    }

    /**
     * Gets the generator.
     *
     * @param element the Element
     * @return the Generator
     */
    @Nullable
    protected JavaDocGenerator getGenerator(@NotNull PsiElement element) {
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

    /**
     * Gets the java element.
     *
     * @param element the Element
     * @return the Java element
     */
    @NotNull
    private PsiElement getJavaElement(@NotNull PsiElement element) {
        PsiElement result = element;
        PsiField field = PsiTreeUtil.getParentOfType(element, PsiField.class);
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (field != null) {
            result = field;
        } else if (method != null) {
            result = method;
        } else if (clazz != null) {
            result = clazz;
        }
        return result;
    }

    private boolean isElementInSelection(@NotNull PsiElement element, int startPosition, int endPosition) {
        boolean result = false;
        int elementTextOffset = element.getTextRange().getStartOffset();
        if (elementTextOffset >= startPosition &&
                elementTextOffset <= endPosition) {
            result = true;
        }
        return result;
    }

    private boolean isAllowedElementType(@NotNull PsiElement element) {
        boolean result = false;
        if (element instanceof PsiClass ||
                element instanceof PsiField ||
                element instanceof PsiMethod) {
            result = true;
        }
        return result;
    }

}
