package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.BeforeClass;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MethodJavaDocGeneratorTemplatesTest extends LightJavaCodeInsightFixtureTestCase {

    private MethodJavaDocGenerator methodJavaDocGenerator;
    private PsiElementFactory elementFactory;
    private PsiFile psiFile;
    private PsiMethod publicSetMethod;
    private PsiMethod publicMethodWithComments;

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        methodJavaDocGenerator = new MethodJavaDocGenerator(getProject());
        elementFactory = PsiElementFactory.getInstance(getProject());
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        settings.getConfiguration().getGeneralSettings().setMode(Mode.UPDATE);
        Map<String, String> classTemplates = settings.getConfiguration().getTemplateSettings().getClassTemplates();
        classTemplates.clear();
        classTemplates.put(".+", "" +
                "/**\\n\n" +
                " * ${name} ${return}.\\n\n" +
                " * \\n\n" +
                " * @param ${parameter.name} the ${paramNames[parameter.name]}\\n\n" +
                " *\\n\n" +
                " * @return the ${return}\\n\n" +
                " */");
        settings.setupTemplates();
        psiFile = PsiFileFactory.getInstance(getProject()).createFileFromText(
                "Test.java", JavaFileType.INSTANCE, "public class Test {}");
        publicMethodWithComments = elementFactory.createMethodFromText(
                "/** " +
                        " * set params description" +
                        " * @param int the parameter" +
                        " * @param config the config" +
                        " */" +
                        "public String setParam(String param, String config) {}", psiFile);
        publicSetMethod = elementFactory.createMethodFromText("public String setParam(String param) {}", psiFile);
    }

    public void testGenerateJavaDoc() {
        JavaDoc javaDoc = methodJavaDocGenerator.generate(publicSetMethod);
        assertThat(javaDoc, notNullValue());

        assertThat(javaDoc.getTags().get("param"), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(0), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(0).getValue(), is("param"));
        assertThat(javaDoc.getTags().get("param").get(0).getDescription().get(0), is("the param"));

        assertThat(javaDoc.getTags().get("return"), notNullValue());
        assertThat(javaDoc.getTags().get("return").get(0), notNullValue());
        assertThat(javaDoc.getTags().get("return").get(0).getValue(), is("the param"));
        assertThat(javaDoc.getTags().get("return").get(0).getDescription().size(), is(0));
    }

    public void testUpdateJavaDoc() {
        JavaDoc javaDoc = methodJavaDocGenerator.generate(publicMethodWithComments);
        assertThat(javaDoc, notNullValue());

        assertThat(javaDoc.getTags().get("param"), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(0), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(0).getValue(), is("param"));
        assertThat(javaDoc.getTags().get("param").get(0).getDescription().get(0), is("the param"));

        assertThat(javaDoc.getTags().get("param"), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(1), notNullValue());
        assertThat(javaDoc.getTags().get("param").get(1).getValue(), is("config"));
        assertThat(javaDoc.getTags().get("param").get(1).getDescription().get(0), is("the config"));

        assertThat(javaDoc.getTags().get("return"), notNullValue());
        assertThat(javaDoc.getTags().get("return").get(0), notNullValue());
        assertThat(javaDoc.getTags().get("return").get(0).getValue(), is("the param"));
        assertThat(javaDoc.getTags().get("return").get(0).getDescription().size(), is(0));
    }
}