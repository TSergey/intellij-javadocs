package com.github.setial.intellijjavadocs.action;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.util.IncorrectOperationException;

/**
 * The type Java doc handler.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocHandler extends GenerateMembersHandlerBase {

    /**
     * Instantiates a new Java docs generate handler.
     */
    public JavaDocHandler() {
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
