package com.github.setial.intellijjavadocs.action;

import com.github.setial.intellijjavadocs.exception.TemplateNotFoundException;
import com.github.setial.intellijjavadocs.generator.JavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.ClassJavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.FieldJavaDocGenerator;
import com.github.setial.intellijjavadocs.generator.impl.MethodJavaDocGenerator;
import com.github.setial.intellijjavadocs.operation.JavaDocWriter;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
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

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Java doc generate action.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocGenerateAction extends BaseAction {

    private static final Logger LOGGER = Logger.getInstance(JavaDocGenerateAction.class);

    private JavaDocWriter writer;

    /**
     * Instantiates a new Java doc generate action.
     */
    public JavaDocGenerateAction() {
        this(new JavaDocHandler());
    }

    /**
     * Instantiates a new Java doc generate action.
     *
     * @param handler the handler
     */
    public JavaDocGenerateAction(CodeInsightActionHandler handler) {
        super(handler);
        writer = ApplicationManager.getApplication().getComponent(JavaDocWriter.class);
    }

    /**
     * Action performed.
     *
     * @param e the Event
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        DumbService dumbService = DumbService.getInstance(e.getProject());
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("Javadocs plugin is not available during indexing");
            return;
        }

        Editor editor = DataKeys.EDITOR.getData(e.getDataContext());
        if (editor == null) {
            LOGGER.error("Cannot get com.intellij.openapi.editor.Editor");
            Messages.showErrorDialog("Javadocs plugin is not available", "Javadocs plugin");
            return;
        }
        int startPosition = editor.getSelectionModel().getSelectionStart();
        int endPosition = editor.getSelectionModel().getSelectionEnd();
        PsiFile file = DataKeys.PSI_FILE.getData(e.getDataContext());
        if (file == null) {
            LOGGER.error("Cannot get com.intellij.psi.PsiFile");
            Messages.showErrorDialog("Javadocs plugin is not available", "Javadocs plugin");
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
                if (element == null) {
                    break;
                }
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
            try {
                @SuppressWarnings("unchecked")
                PsiDocComment javaDoc = generator.generate(element);
                if (javaDoc != null) {
                    writer.write(javaDoc, element);
                }
            } catch (TemplateNotFoundException e) {
                LOGGER.warn(e);
                String message = "Javadocs plugin is not available. Can not find suitable template for the element:\n{0}";
                Messages.showWarningDialog(MessageFormat.format(message, e.getMessage()), "Javadocs plugin");
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
