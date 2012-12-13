package com.github.ideajavadocs.action;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.github.ideajavadocs.generator.impl.ClassJavaDocGenerator;
import com.github.ideajavadocs.generator.impl.FieldJavaDocGenerator;
import com.github.ideajavadocs.generator.impl.MethodJavaDocGenerator;
import com.github.ideajavadocs.operation.JavaDocWriter;
import com.github.ideajavadocs.operation.impl.JavaDocWriterImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JavaDocGenerateAction extends AnAction {

    private static final Map<Class<? extends PsiElement>, JavaDocGenerator> gen =
            new HashMap<Class<? extends PsiElement>, JavaDocGenerator>();

    static {
        gen.put(PsiClass.class, new ClassJavaDocGenerator());
        gen.put(PsiMethod.class, new MethodJavaDocGenerator());
        gen.put(PsiField.class, new FieldJavaDocGenerator());
    }

    private JavaDocWriter writer;

    public JavaDocGenerateAction() {
        writer = new JavaDocWriterImpl();
    }

    public void actionPerformed(AnActionEvent e) {
        Editor editor = DataKeys.EDITOR.getData(e.getDataContext());
        if (editor == null) {
            // TODO show message
            return;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiFile file = DataKeys.PSI_FILE.getData(e.getDataContext());
        if (file == null) {
            // TODO show message
            return;
        }
        PsiElement element = getJavaElement(PsiUtilCore.getElementAtOffset(file, offset));
        @SuppressWarnings("unchecked")
        PsiDocComment javaDoc = getGenerator(element.getClass()).generate(element, false);
        writer.write(javaDoc, element);
    }

    @NotNull
    private PsiElement getJavaElement(@NotNull PsiElement element) {
        PsiElement result = null;
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

    @NotNull
    private JavaDocGenerator getGenerator(@NotNull Class<? extends PsiElement> clazz) {
        JavaDocGenerator generator = null;
        for (Class<? extends PsiElement> elementClass : gen.keySet()) {
            if (elementClass.isAssignableFrom(clazz)) {
                generator = gen.get(elementClass);
                break;
            }
        }
        return generator;
    }

}
