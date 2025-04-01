package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.GeneralSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.google.common.collect.Sets;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase4;
import org.junit.Before;
import org.junit.Test;

import static com.intellij.openapi.application.ActionsKt.runReadAction;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodJavaDocGeneratorTest extends LightJavaCodeInsightFixtureTestCase4 {

    private MethodJavaDocGenerator methodJavaDocGenerator;
    private PsiElementFactory elementFactory;
    private GeneralSettings generalSettings;
    private PsiFile psiFile;
    private PsiMethod publicGetMethod;
    private PsiMethod protectedGetMethod;

    @Before
    public void setUp() {
        methodJavaDocGenerator = new MethodJavaDocGenerator(getProject());
        elementFactory = PsiElementFactory.getInstance(getProject());
        JavaDocConfiguration settings = getProject().getService(JavaDocConfiguration.class);
        generalSettings = settings.getConfiguration().getGeneralSettings();
        generalSettings.setMode(Mode.REPLACE);
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(getProject());
        psiFile = runReadAction(() -> fileFactory.createFileFromText(
                "Test.java", JavaFileType.INSTANCE, "public class Test {}"));
        publicGetMethod = runReadAction(() -> elementFactory.createMethodFromText("public String getParam(String param) {}", psiFile));
        protectedGetMethod = runReadAction(() -> elementFactory.createMethodFromText("protected String getParam(String param) {}", psiFile));
    }

    @Test
    public void testGenerateJavaDoc() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, notNullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoMethodLevel() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoMethodLevelAndPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDocProtected() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(protectedGetMethod));
        assertThat(javaDoc, notNullValue());
    }

    @Test
    public void testGenerateJavaDocProtected_NoProtectedVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(protectedGetMethod));
        assertThat(javaDoc, nullValue());
    }

    private Project getProject() {
        return getFixture().getProject();
    }
}