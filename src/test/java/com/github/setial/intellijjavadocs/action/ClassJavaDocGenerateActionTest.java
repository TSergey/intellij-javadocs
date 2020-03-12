package com.github.setial.intellijjavadocs.action;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.TemplateSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.Before;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassJavaDocGenerateActionTest extends LightJavaCodeInsightFixtureTestCase {

    public static final String GENERATE_ACTION_ON_EDITOR = "com.github.setial.intellijjavadocs.action.JavaDocGenerateActionOnEditor";
    private TemplateSettings origTemplateSettings;

    @Before
    public void setup() throws Exception {
        super.setUp();
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        settings.getConfiguration().getGeneralSettings().setMode(Mode.REPLACE);

        TemplateSettings templateSettings = settings.getConfiguration().getTemplateSettings();
        if (origTemplateSettings == null) {
            origTemplateSettings = new TemplateSettings();
            origTemplateSettings.setClassTemplates(new LinkedHashMap<>(templateSettings.getClassTemplates()));
            origTemplateSettings.setConstructorTemplates(new LinkedHashMap<>(templateSettings.getConstructorTemplates()));
            origTemplateSettings.setFieldTemplates(new LinkedHashMap<>(templateSettings.getFieldTemplates()));
            origTemplateSettings.setMethodTemplates(new LinkedHashMap<>(templateSettings.getMethodTemplates()));
        } else {
            settings.getConfiguration().setTemplateSettings(origTemplateSettings);
            settings.setupTemplates();
        }
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/class";
    }

    public void testClassJavaDocsReplace() {
        doTest("ClassJavaDocsReplace");
    }

    public void testClassJavaDocsUpdate() {
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        settings.getConfiguration().getGeneralSettings().setMode(Mode.UPDATE);
        doTest("ClassJavaDocsUpdate");
    }

    public void testClassJavaDocsWithAttributes() {
        setupCustomClassTemplateWithAttributes();
        doTest("ClassJavaDocsWithAttributes");
    }

    public void testClassJavaDocsUpdateWithAttributes() {
        setupCustomClassTemplateWithAttributes();
        doTest("ClassJavaDocsUpdateWithAttributes");
    }

    public void testClassJavaDocsDateAttribute() {
        setupCustomClassTemplateWithDateAttribute();
        doTest("ClassJavaDocsDateAttribute");
    }

    private void setupCustomClassTemplateWithDateAttribute() {
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        Map<String, String> classTemplates = settings.getConfiguration().getTemplateSettings().getClassTemplates();
        classTemplates.clear();
        classTemplates.put(".+", "" +
                "/**\\n\n" +
                " * The type ${name}.\\n\n" +
                " * @date 2019-08-06 15:50 \\n\n" +
                " * @date 2019.08.06 15:50 \\n\n" +
                " * @date 2019/08/06 15:50 \\n\n" +
                " */");
        settings.setupTemplates();
    }

    private void setupCustomClassTemplateWithAttributes() {
        JavaDocConfiguration settings = ServiceManager.getService(getProject(), JavaDocConfiguration.class);
        settings.getConfiguration().getGeneralSettings().setMode(Mode.UPDATE);
        Map<String, String> classTemplates = settings.getConfiguration().getTemplateSettings().getClassTemplates();
        classTemplates.clear();
        classTemplates.put(".+", "" +
                "/**\\n\n" +
                " * The type ${name}.\\n\n" +
                " * @author John Smith\\n\n" +
                " * @since 2020-02-23 \\n\n" +
                " */");
        settings.setupTemplates();
    }

    private void doTest(String classJavadocTestData) {
        myFixture.configureByFile(classJavadocTestData + ".given.java");
        myFixture.performEditorAction(GENERATE_ACTION_ON_EDITOR);
        myFixture.checkResultByFile(classJavadocTestData + ".expected.java");
    }
}