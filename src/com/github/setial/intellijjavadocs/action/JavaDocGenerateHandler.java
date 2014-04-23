package com.github.setial.intellijjavadocs.action;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.util.IncorrectOperationException;

/**
 * The type Java doc generate handler.
 */
public class JavaDocGenerateHandler extends GenerateMembersHandlerBase {

    /**
     * Instantiates a new Java doc generate handler.
     */
    public JavaDocGenerateHandler() {
        super("");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass aClass) {
        return new ClassMember[0];
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass aClass, ClassMember originalMember) throws IncorrectOperationException {
        return new GenerationInfo[0];
    }

}
