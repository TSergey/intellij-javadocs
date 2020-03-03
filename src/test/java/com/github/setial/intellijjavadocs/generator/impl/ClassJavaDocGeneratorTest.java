package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ClassJavaDocGeneratorTest extends LightJavaCodeInsightFixtureTestCase {

    private static final String DATE = "2020-02-26 12:57";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Stone";

    private PsiElementFactory elementFactory;
    private ClassJavaDocGenerator classJavaDocGenerator;
    private PsiClass publicClass;
    private PsiClass publicClassWithComments;

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        classJavaDocGenerator = new ClassJavaDocGenerator(getProject());
        elementFactory = PsiElementFactory.getInstance(getProject());
        PsiJavaFile psiFile = (PsiJavaFile) PsiFileFactory.getInstance(getProject()).createFileFromText(
                "MyClass.java", JavaFileType.INSTANCE,
                "public class MyClass {}");
        publicClass = psiFile.getClasses()[0];
        PsiJavaFile psiFileWithComments = (PsiJavaFile) PsiFileFactory.getInstance(getProject()).createFileFromText(
                "MyClass.java", JavaFileType.INSTANCE,
                "/**\n * My class description\n */\npublic class MyClass {}");
        publicClassWithComments = psiFileWithComments.getClasses()[0];

        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        settings.getConfiguration().getGeneralSettings().setMode(Mode.UPDATE);
        Map<String, String> classTemplates = settings.getConfiguration().getTemplateSettings().getClassTemplates();
        classTemplates.clear();
        classTemplates.put(".+", "" +
                "/**\\n\n" +
                " * The type ${name}.\\n\n" +
                " * @author " + FIRST_NAME + " " + LAST_NAME + "\\n\n" +
                " * @since " + DATE + " \\n\n" +
                " */");
        settings.setupTemplates();
    }

    public void testGenerateJavaDoc() {
        JavaDoc javaDoc = classJavaDocGenerator.generate(publicClass);
        assertThat(javaDoc, notNullValue());

        assertThat(javaDoc.getDescription(), notNullValue());
        assertThat(javaDoc.getTags().get("author"), notNullValue());
        assertThat(javaDoc.getTags().get("author").get(0), notNullValue());
        assertThat(StringUtils.trim(javaDoc.getTags().get("author").get(0).getValue()), is(FIRST_NAME + " " + LAST_NAME));
        assertThat(javaDoc.getTags().get("author").get(0).getDescription().size(), is(0));

        assertThat(javaDoc.getTags().get("since"), notNullValue());
        assertThat(javaDoc.getTags().get("since").get(0), notNullValue());
        assertThat(StringUtils.trim(javaDoc.getTags().get("since").get(0).getValue()), is(DATE));
        assertThat(javaDoc.getTags().get("since").get(0).getDescription().size(), is(0));
    }

    public void testUpdateJavaDoc() {
        JavaDoc javaDoc = classJavaDocGenerator.generate(publicClassWithComments);
        assertThat(javaDoc, notNullValue());

        assertThat(javaDoc.getDescription().get(1), is("My class description"));
        assertThat(javaDoc.getTags().get("author"), notNullValue());
        assertThat(javaDoc.getTags().get("author").get(0), notNullValue());
        assertThat(StringUtils.trim(javaDoc.getTags().get("author").get(0).getValue()), is(FIRST_NAME + " " + LAST_NAME));
        assertThat(javaDoc.getTags().get("author").get(0).getDescription().size(), is(0));

        assertThat(javaDoc.getTags().get("since"), notNullValue());
        assertThat(javaDoc.getTags().get("since").get(0), notNullValue());
        assertThat(StringUtils.trim(javaDoc.getTags().get("since").get(0).getValue()), is(DATE));
        assertThat(javaDoc.getTags().get("since").get(0).getDescription().size(), is(0));
    }
}