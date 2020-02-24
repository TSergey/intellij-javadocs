package com.github.setial.intellijjavadocs.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.psi.PsiClass;

/**
 * The type Base action.
 *
 * @author Sergey Timofiychuk
 */
public class BaseAction extends BaseGenerateAction {

    /**
     * Instantiates a new Base action.
     *
     * @param handler the handler
     */
    public BaseAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(final PsiClass targetClass) {
        return true;
    }
}
