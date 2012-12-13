package com.github.ideajavadocs.generator.impl;

import com.github.ideajavadocs.model.JavaDoc;
import com.github.ideajavadocs.model.JavaDocElements;
import com.github.ideajavadocs.model.JavaDocTag;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClassJavaDocGenerator extends AbstractJavaDocGenerator<PsiClass> {

    @Override
    @NotNull
    protected JavaDoc generate(@NotNull PsiClass element) {
        // TODO

        List<String> description = new LinkedList<String>();
        description.add(JavaDocElements.NEW_LINE.getPresentation());
        description.add("The class " + element.getName());
        description.add(JavaDocElements.NEW_LINE.getPresentation());
        Map<String, JavaDocTag> tags = new LinkedHashMap<String, JavaDocTag>();
        tags.put("author", new JavaDocTag(StringUtils.EMPTY, "test", Collections.<String>emptyList()));
        return new JavaDoc(description, tags);
    }

}
