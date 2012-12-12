package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.generator.JavaDocGenerator;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;

public class MethodJavaDocGenerator implements JavaDocGenerator<PsiMethod> {

    @Override
    public PsiDocComment generate(PsiMethod element, boolean replace) {
        PsiDocComment oldDocComment = null;
        if (element.getFirstChild() instanceof PsiDocComment) {
            oldDocComment = (PsiDocComment) element.getFirstChild();
        }
        if (oldDocComment != null) {

            // TODO merge or replace javadoc
        }

        String name = element.getName();
        PsiParameterList parameterList = element.getParameterList();
        PsiType returnType = element.getReturnType();
        PsiClassType[] throwsList = element.getThrowsList().getReferencedTypes();
        boolean isConstructor = element.isConstructor();

        StringBuilder result = new StringBuilder();
        result
                .append("/**\n")
                .append("* The ")
                .append(name);

        if (isConstructor) {
            result.append(" constructor.");
        } else {
            result.append(" method.");
        }
        result.append("*\n");
        if (parameterList.getParametersCount() > 0) {
            for (PsiParameter parameter : parameterList.getParameters()) {
                String paramType = parameter.getType().getCanonicalText();
                result.append("* @param ")
                        .append(parameter.getName())
                        .append(" the ")
                        .append(paramType)
                        .append("\n");
            }
        }
        if (returnType != null && !returnType.isAssignableFrom(PsiType.VOID)) {
            result.append("* @return ")
                    .append(returnType.getCanonicalText())
                    .append("\n");
        }
        if (throwsList.length > 0) {
            for (PsiClassType throwsType : throwsList) {
                result.append("* @throws")
                        .append(throwsType.getCanonicalText())
                        .append("\n");
            }
        }

        result.append("*/\n");

        PsiElementFactory psiElementFactory = PsiElementFactory.SERVICE.getInstance(element.getProject());
        PsiDocComment docElement = psiElementFactory.createDocCommentFromText(result.toString());

        return docElement;
    }

}
