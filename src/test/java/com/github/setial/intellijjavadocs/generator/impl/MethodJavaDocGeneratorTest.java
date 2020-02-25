package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.GeneralSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.google.common.collect.Sets;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.BeforeClass;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MethodJavaDocGeneratorTest extends LightJavaCodeInsightFixtureTestCase {

    private MethodJavaDocGenerator methodJavaDocGenerator;
    private PsiElementFactory elementFactory;
    private GeneralSettings generalSettings;
    private PsiFile psiFile;
    private PsiMethod publicGetMethod;
    private PsiMethod protectedGetMethod;

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        methodJavaDocGenerator = new MethodJavaDocGenerator(getProject());
        elementFactory = PsiElementFactory.getInstance(getProject());
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        generalSettings = settings.getConfiguration().getGeneralSettings();
        generalSettings.setMode(Mode.REPLACE);
        psiFile = PsiFileFactory.getInstance(getProject()).createFileFromText(
                "Test.java", JavaFileType.INSTANCE, "public class Test {}");
        publicGetMethod = elementFactory.createMethodFromText("public String getParam(String param) {}", psiFile);
        protectedGetMethod = elementFactory.createMethodFromText("protected String getParam(String param) {}", psiFile);
    }

    public void testGenerateJavaDoc() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(publicGetMethod);
        assertThat(javaDoc, notNullValue());
    }

    public void testGenerateJavaDoc_NoPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(publicGetMethod);
        assertThat(javaDoc, nullValue());
    }

    public void testGenerateJavaDoc_NoMethodLevel() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(publicGetMethod);
        assertThat(javaDoc, nullValue());
    }

    public void testGenerateJavaDoc_NoMethodLevelAndPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(publicGetMethod);
        assertThat(javaDoc, nullValue());
    }

    public void testGenerateJavaDocProtected() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(protectedGetMethod);
        assertThat(javaDoc, notNullValue());
    }

    public void testGenerateJavaDocProtected_NoProtectedVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE));

        JavaDoc javaDoc = methodJavaDocGenerator.generateJavaDoc(protectedGetMethod);
        assertThat(javaDoc, nullValue());
    }
}