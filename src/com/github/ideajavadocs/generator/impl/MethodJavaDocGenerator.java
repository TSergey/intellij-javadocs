package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.transformation.JavaDocUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Method java doc generator.
 *
 * @author Sergey Timofiychuk
 */
public class MethodJavaDocGenerator extends AbstractJavaDocGenerator<PsiMethod> {

    /**
     * Instantiates a new Method java doc generator.
     *
     * @param project the Project
     */
    public MethodJavaDocGenerator(@NotNull Project project) {
        super(project);
    }

    @Nullable
    @Override
    protected JavaDoc generateJavaDoc(@NotNull PsiMethod element) {
        if (!shouldGenerate(element) || !shouldGenerate(element.getModifierList())) {
            return null;
        }
        Template template = getDocTemplateManager().getMethodTemplate(element);
        Map<String, String> paramNames = new HashMap<String, String>();
        for (PsiParameter parameter : element.getParameterList().getParameters()) {
            paramNames.put(parameter.getName(), getDocTemplateProcessor().buildDescription(parameter.getName()));
        }
        Map<String, String> exceptionNames = new HashMap<String, String>();
        for (PsiJavaCodeReferenceElement exception : element.getThrowsList().getReferenceElements()) {
            exceptionNames.put(exception.getReferenceName(), getDocTemplateProcessor().buildDescription(exception.getReferenceName()));
        }
        String returnDescription = StringUtils.EMPTY;
        PsiTypeElement returnElement = element.getReturnTypeElement();
        if (returnElement != null) {
            returnDescription = returnElement.getText();
        }
        Map<String, Object> params = getDefaultParameters(element);
        if (returnElement != null) {
            params.put("isNotVoid", !returnElement.getType().isAssignableFrom(PsiType.VOID));
            params.put("return", getDocTemplateProcessor().buildDescription(returnDescription));
        }
        params.put("paramNames", paramNames);
        params.put("exceptionNames", exceptionNames);


        String javaDocText = getDocTemplateProcessor().merge(template, params);
        return JavaDocUtils.toJavaDoc(javaDocText, getPsiElementFactory());
    }

    private boolean shouldGenerate(@NotNull PsiMethod element) {
        PsiMethod[] superMethods = element.findSuperMethods();
        boolean overriddenMethods = superMethods.length > 0 && !getSettings().getConfiguration().getGeneralSettings().isOverriddenMethods();
        boolean level = getSettings().getConfiguration().getGeneralSettings().getLevels().contains(Level.METHOD);
        return !level || !overriddenMethods;
    }

}
