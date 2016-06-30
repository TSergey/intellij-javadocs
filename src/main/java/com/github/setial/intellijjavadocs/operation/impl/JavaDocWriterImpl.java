package com.github.setial.intellijjavadocs.operation.impl;

import com.github.setial.intellijjavadocs.exception.FileNotValidException;
import com.github.setial.intellijjavadocs.exception.NotFoundElementException;
import com.github.setial.intellijjavadocs.operation.JavaDocWriter;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.ReadonlyStatusHandler.OperationStatus;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * The type Java doc writer impl.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocWriterImpl implements JavaDocWriter {

    /**
     * The constant COMPONENT_NAME.
     */
    public static final String COMPONENT_NAME = "JavaDocWriter";

    private static final Logger LOGGER = Logger.getInstance(JavaDocWriterImpl.class);

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;

    }

    @Override
    public void write(@NotNull PsiDocComment javaDoc, @NotNull PsiElement beforeElement) {
        try {
            checkFilesAccess(beforeElement);
        } catch (FileNotValidException e) {
            LOGGER.error(e.getMessage());
            Messages.showErrorDialog("Javadocs plugin is not available, cause: " + e.getMessage(), "Javadocs plugin");
            return;
        }

        WriteCommandAction command = new WriteJavaDocActionImpl(javaDoc, beforeElement);
        RunResult result = command.execute();
        if (result.hasException()) {
            LOGGER.error(result.getThrowable());
            Messages.showErrorDialog("Javadocs plugin is not available, cause: " + result.getThrowable().getMessage(), "Javadocs plugin");
        }
    }

    @Override
    public void remove(@NotNull PsiElement beforeElement) {
        try {
            checkFilesAccess(beforeElement);
        } catch (FileNotValidException e) {
            LOGGER.error(e);
            Messages.showErrorDialog("Javadocs plugin is not available, cause: " + e.getMessage(), "Javadocs plugin");
            return;
        }

        WriteCommandAction command = new RemoveJavaDocActionImpl(beforeElement);
        RunResult result = command.execute();
        if (result.hasException()) {
            LOGGER.error(result.getThrowable());
            Messages.showErrorDialog("Javadocs plugin is not available, cause: " + result.getThrowable().getMessage(), "Javadocs plugin");
        }
    }

    /**
     * The type Write command action impl.
     *
     * @author Sergey Timofiychuk
     */
    private static class WriteJavaDocActionImpl extends WriteCommandAction {

        private PsiDocComment javaDoc;
        private PsiElement element;

        /**
         * Instantiates a new Write java doc action impl.
         *
         * @param javaDoc the java doc
         * @param element the element
         */
        protected WriteJavaDocActionImpl(@NotNull PsiDocComment javaDoc, @NotNull PsiElement element) {
            super(
                    element.getProject(),
                    WRITE_JAVADOC_COMMAND_NAME,
                    WRITE_JAVADOC_COMMAND_GROUP,
                    element.getContainingFile());
            this.javaDoc = javaDoc;
            this.element = element;
        }

        @Override
        protected void run(@NotNull Result result) throws Throwable {
            if (javaDoc == null) {
                return;
            }
            if (element.getFirstChild() instanceof PsiDocComment) {
                replaceJavaDoc(element, javaDoc);
            } else {
                addJavaDoc(element, javaDoc);
            }
            ensureWhitespaceAfterJavaDoc(element);
            reformatJavaDoc(element);
        }

        private void ensureWhitespaceAfterJavaDoc(PsiElement element) {
            // this method is required to create well formatted javadocs in enums
            PsiElement firstChild = element.getFirstChild();
            if (!PsiDocComment.class.isAssignableFrom(firstChild.getClass())) {
                return;
            }
            PsiElement nextElement = firstChild.getNextSibling();
            if (PsiWhiteSpace.class.isAssignableFrom(nextElement.getClass())) {
                return;
            }
            pushPostponedChanges(element);
            element.getNode().addChild(new PsiWhiteSpaceImpl("\n"), nextElement.getNode());
        }
    }

    private static class RemoveJavaDocActionImpl extends WriteCommandAction {

        private PsiElement element;

        /**
         * Instantiates a new Remove java doc action impl.
         *
         * @param element the element
         */
        protected RemoveJavaDocActionImpl(PsiElement element) {
            super(
                    element.getProject(),
                    WRITE_JAVADOC_COMMAND_NAME,
                    WRITE_JAVADOC_COMMAND_GROUP,
                    element.getContainingFile());
            this.element = element;
        }

        @Override
        protected void run(@NotNull Result result) throws Throwable {
            if (element.getFirstChild() instanceof PsiDocComment) {
                deleteJavaDoc(this.element);
            }
        }

    }

    private static void deleteJavaDoc(PsiElement theElement) {
        pushPostponedChanges(theElement);
        theElement.getFirstChild().delete();
    }

    private static void addJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
        pushPostponedChanges(theElement);
        theElement.getNode().addChild(theJavaDoc.getNode(), theElement.getFirstChild().getNode());
    }

    private static void replaceJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
        deleteJavaDoc(theElement);
        addJavaDoc(theElement, theJavaDoc);
    }

    private static void reformatJavaDoc(PsiElement theElement) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(theElement.getProject());
        pushPostponedChanges(theElement);
        try {
            int javadocTextOffset = findJavaDocTextOffset(theElement);
            int javaCodeTextOffset = findJavaCodeTextOffset(theElement);
            codeStyleManager.reformatText(theElement.getContainingFile(), javadocTextOffset, javaCodeTextOffset + 1);
        } catch (NotFoundElementException e) {
            LOGGER.info("Could not reformat javadoc since cannot find required elements", e);
        }
    }

    private static int findJavaDocTextOffset(PsiElement theElement) {
        PsiElement javadocElement = theElement.getFirstChild();
        if (!(javadocElement instanceof PsiDocComment)) {
            throw new NotFoundElementException("Cannot find element of type PsiDocComment");
        }
        return javadocElement.getTextOffset();
    }

    private static int findJavaCodeTextOffset(PsiElement theElement) {
        if (theElement.getChildren().length < 2) {
            throw new NotFoundElementException("Can not find offset of java code");
        }
        return theElement.getChildren()[1].getTextOffset();
    }

    private static void pushPostponedChanges(PsiElement element) {
        Editor editor = PsiUtilBase.findEditor(element.getContainingFile());
        if (editor != null) {
            PsiDocumentManager.getInstance(element.getProject())
                    .doPostponedOperationsAndUnblockDocument(editor.getDocument());
        }
    }

    private void checkFilesAccess(@NotNull PsiElement beforeElement) {
        PsiFile containingFile = beforeElement.getContainingFile();
        if (containingFile == null || !containingFile.isValid()) {
            throw new FileNotValidException("File cannot be used to generate javadocs");
        }
        OperationStatus status = ReadonlyStatusHandler.getInstance(beforeElement.getProject()).
                ensureFilesWritable(Collections.singletonList(containingFile.getVirtualFile()));
        if (status.hasReadonlyFiles()) {
            throw new FileNotValidException(status.getReadonlyFilesMessage());
        }
    }
}
