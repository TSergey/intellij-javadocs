package com.github.setial.intellijjavadocs.ui.settings;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.github.setial.intellijjavadocs.ui.component.TemplatesTable;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigPanel extends JPanel {

    private JavaDocSettings settings;

    private JTabbedPane tabbedPane;
    private JPanel panel;
    private JRadioButton generalModeKeepRadioButton;
    private JRadioButton generalModeUpdateRadioButton;
    private JRadioButton generalModeReplaceRadioButton;
    private JCheckBox generalLevelTypeCheckbox;
    private JCheckBox generalLevelMethodCheckbox;
    private JCheckBox generalLevelFieldCheckbox;
    private JCheckBox generalVisibilityPublicCheckbox;
    private JCheckBox generalVisibilityProtectedCheckbox;
    private JCheckBox generalVisibilityDefaultCheckbox;
    private JCheckBox generalVisibilityPrivateCheckbox;
    private JCheckBox generalOtherOverriddenMethodsCheckbox;
    private JPanel generalPanel;
    private JPanel generalModePanel;
    private JPanel generalLevelPanel;
    private JPanel generalVisibilityPanel;
    private JPanel generalOtherPanel;
    private TemplatesTable classTemplatesTable;
    private TemplatesTable constructorTemplatesTable;
    private TemplatesTable methodTemplatesTable;
    private TemplatesTable fieldTemplatesTable;

    public ConfigPanel(JavaDocSettings settings) {
        this.settings = settings;
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        setupBorders();
        setupTemplatesPanel();
    }

    public boolean isModified() {
        boolean result = false;
        // check if general settings are modified
        if (generalModeKeepRadioButton.isSelected()) {
            result = settings.getGeneralSettings().getMode() != Mode.KEEP;
        } else if (generalModeUpdateRadioButton.isSelected()) {
            result = settings.getGeneralSettings().getMode() != Mode.UPDATE;
        } else if (generalModeReplaceRadioButton.isSelected()) {
            result = settings.getGeneralSettings().getMode() != Mode.REPLACE;
        }
        result = result || isCheckboxModified(generalLevelTypeCheckbox, settings.getGeneralSettings().getLevels().contains(Level.TYPE));
        result = result || isCheckboxModified(generalLevelMethodCheckbox, settings.getGeneralSettings().getLevels().contains(Level.METHOD));
        result = result || isCheckboxModified(generalLevelFieldCheckbox, settings.getGeneralSettings().getLevels().contains(Level.FIELD));
        result = result || isCheckboxModified(
                generalVisibilityPublicCheckbox, settings.getGeneralSettings().getVisibilities().contains(Visibility.PUBLIC));
        result = result || isCheckboxModified(
                generalVisibilityProtectedCheckbox, settings.getGeneralSettings().getVisibilities().contains(Visibility.PROTECTED));
        result = result || isCheckboxModified(
                generalVisibilityDefaultCheckbox, settings.getGeneralSettings().getVisibilities().contains(Visibility.DEFAULT));
        result = result || isCheckboxModified(
                generalVisibilityPrivateCheckbox, settings.getGeneralSettings().getVisibilities().contains(Visibility.PRIVATE));
        result = result || isCheckboxModified(generalOtherOverriddenMethodsCheckbox, settings.getGeneralSettings().isOverriddenMethods());

        // check if templates settings are modified
        result = result || checkIfTableContentModified(classTemplatesTable.getSettings(),
                settings.getTemplateSettings().getClassTemplates());
        result = result || checkIfTableContentModified(constructorTemplatesTable.getSettings(),
                settings.getTemplateSettings().getConstructorTemplates());
        result = result || checkIfTableContentModified(methodTemplatesTable.getSettings(),
                settings.getTemplateSettings().getMethodTemplates());
        result = result || checkIfTableContentModified(fieldTemplatesTable.getSettings(),
                settings.getTemplateSettings().getFieldTemplates());

        return result;
    }

    public void apply() {
        // apply general settings
        if (generalModeKeepRadioButton.isSelected()) {
            settings.getGeneralSettings().setMode(Mode.KEEP);
        } else if (generalModeUpdateRadioButton.isSelected()) {
            settings.getGeneralSettings().setMode(Mode.UPDATE);
        } else if (generalModeReplaceRadioButton.isSelected()) {
            settings.getGeneralSettings().setMode(Mode.REPLACE);
        }

        settings.getGeneralSettings().getLevels().clear();
        if (generalLevelTypeCheckbox.isSelected()) {
            settings.getGeneralSettings().getLevels().add(Level.TYPE);
        }
        if (generalLevelMethodCheckbox.isSelected()) {
            settings.getGeneralSettings().getLevels().add(Level.METHOD);
        }
        if (generalLevelFieldCheckbox.isSelected()) {
            settings.getGeneralSettings().getLevels().add(Level.FIELD);
        }

        settings.getGeneralSettings().getVisibilities().clear();
        if (generalVisibilityPublicCheckbox.isSelected()) {
            settings.getGeneralSettings().getVisibilities().add(Visibility.PUBLIC);
        }
        if (generalVisibilityProtectedCheckbox.isSelected()) {
            settings.getGeneralSettings().getVisibilities().add(Visibility.PROTECTED);
        }
        if (generalVisibilityDefaultCheckbox.isSelected()) {
            settings.getGeneralSettings().getVisibilities().add(Visibility.DEFAULT);
        }
        if (generalVisibilityPrivateCheckbox.isSelected()) {
            settings.getGeneralSettings().getVisibilities().add(Visibility.PRIVATE);
        }

        settings.getGeneralSettings().setOverriddenMethods(generalOtherOverriddenMethodsCheckbox.isSelected());

        // apply templates settings
        settings.getTemplateSettings().setClassTemplates(classTemplatesTable.getSettings());
        settings.getTemplateSettings().setConstructorTemplates(constructorTemplatesTable.getSettings());
        settings.getTemplateSettings().setMethodTemplates(methodTemplatesTable.getSettings());
        settings.getTemplateSettings().setFieldTemplates(fieldTemplatesTable.getSettings());
    }

    public void reset() {
        // reset general settings
        switch (settings.getGeneralSettings().getMode()) {
            case KEEP:
                generalModeKeepRadioButton.setSelected(true);
                break;
            case UPDATE:
                generalModeUpdateRadioButton.setSelected(true);
                break;
            case REPLACE:
                generalModeReplaceRadioButton.setSelected(true);
                break;
        }
        for (Level level : settings.getGeneralSettings().getLevels()) {
            switch (level) {
                case TYPE:
                    generalLevelTypeCheckbox.setSelected(true);
                    break;
                case METHOD:
                    generalLevelMethodCheckbox.setSelected(true);
                    break;
                case FIELD:
                    generalLevelFieldCheckbox.setSelected(true);
                    break;
            }
        }
        for (Visibility visibility : settings.getGeneralSettings().getVisibilities()) {
            switch (visibility) {
                case PUBLIC:
                    generalVisibilityPublicCheckbox.setSelected(true);
                    break;
                case PROTECTED:
                    generalVisibilityProtectedCheckbox.setSelected(true);
                    break;
                case DEFAULT:
                    generalVisibilityDefaultCheckbox.setSelected(true);
                    break;
                case PRIVATE:
                    generalVisibilityPrivateCheckbox.setSelected(true);
                    break;
            }
        }
        generalOtherOverriddenMethodsCheckbox.setSelected(settings.getGeneralSettings().isOverriddenMethods());

        // reset templates settings
        classTemplatesTable.setSettingsModel(settings.getTemplateSettings().getClassTemplates());
        constructorTemplatesTable.setSettingsModel(settings.getTemplateSettings().getConstructorTemplates());
        methodTemplatesTable.setSettingsModel(settings.getTemplateSettings().getMethodTemplates());
        fieldTemplatesTable.setSettingsModel(settings.getTemplateSettings().getFieldTemplates());
    }

    public void disposeUIResources() {
    }

    @SuppressWarnings("unchecked")
    private boolean checkIfTableContentModified(Map<String, String> templatesTableSettings,
                                                Map<String, String> templatesSettings) {
        boolean result = false;

        Entry<String, String>[] templatesTableEntries =
                templatesTableSettings.entrySet().toArray(new Entry[templatesTableSettings.size()]);
        Entry<String, String>[] templatesEntries =
                templatesSettings.entrySet().toArray(new Entry[templatesSettings.size()]);
        if (templatesEntries.length == templatesTableEntries.length) {
            for (int i = 0; i < templatesEntries.length; i++) {
                result = result || !templatesEntries[i].getKey().equals(templatesTableEntries[i].getKey());
                result = result || !templatesEntries[i].getValue().equals(templatesTableEntries[i].getValue());
            }
        } else {
            result = true;
        }
        return result;
    }

    private boolean isCheckboxModified(JCheckBox checkbox, boolean oldValue) {
        return checkbox.isSelected() != oldValue;
    }

    private void setupBorders() {
        generalModePanel.setBorder(
                IdeBorderFactory.createTitledBorder("Mode", false, new Insets(0, 0, 0, 10)));
        generalLevelPanel.setBorder(
                IdeBorderFactory.createTitledBorder("Level", false, new Insets(0, 0, 0, 10)));
        generalVisibilityPanel.setBorder(
                IdeBorderFactory.createTitledBorder("Visibility", false, new Insets(0, 0, 0, 0)));
        generalOtherPanel.setBorder(
                IdeBorderFactory.createTitledBorder("Other", false, new Insets(10, 0, 10, 10)));
    }

    private void setupTemplatesPanel() {
        classTemplatesTable = new TemplatesTable(settings.getTemplateSettings().getClassTemplates());
        JPanel classTemplatesLocalPanel = ToolbarDecorator.createDecorator(classTemplatesTable).createPanel();
        JPanel classPanel = new JPanel(new BorderLayout());
        classPanel.setBorder(IdeBorderFactory.createTitledBorder("Class level", false, new Insets(0, 0, 10, 0)));
        classPanel.add(classTemplatesLocalPanel, BorderLayout.CENTER);

        constructorTemplatesTable = new TemplatesTable(settings.getTemplateSettings().getConstructorTemplates());
        JPanel constructorTemplatesLocalPanel =
                ToolbarDecorator.createDecorator(constructorTemplatesTable).createPanel();
        JPanel constructorPanel = new JPanel(new BorderLayout());
        constructorPanel.setBorder(IdeBorderFactory.createTitledBorder("Constructor level", false,
                new Insets(0, 0, 10, 0)));
        constructorPanel.add(constructorTemplatesLocalPanel, BorderLayout.CENTER);

        methodTemplatesTable = new TemplatesTable(settings.getTemplateSettings().getMethodTemplates());
        JPanel methodTemplatesLocalPanel = ToolbarDecorator.createDecorator(methodTemplatesTable).createPanel();
        JPanel methodPanel = new JPanel(new BorderLayout());
        methodPanel.setBorder(IdeBorderFactory.createTitledBorder("Method level", false, new Insets(0, 0, 10, 0)));
        methodPanel.add(methodTemplatesLocalPanel, BorderLayout.CENTER);

        fieldTemplatesTable = new TemplatesTable(settings.getTemplateSettings().getFieldTemplates());
        JPanel fieldTemplatesLocalPanel = ToolbarDecorator.createDecorator(fieldTemplatesTable).createPanel();
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBorder(IdeBorderFactory.createTitledBorder("Field level", false, new Insets(0, 0, 0, 0)));
        fieldPanel.add(fieldTemplatesLocalPanel, BorderLayout.CENTER);

        JPanel templatesPanel = new JPanel();
        templatesPanel.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));

        templatesPanel.add(classPanel, getConstraints(0, 0));
        templatesPanel.add(constructorPanel, getConstraints(1, 0));
        templatesPanel.add(methodPanel, getConstraints(2, 0));
        templatesPanel.add(fieldPanel, getConstraints(3, 0));
        tabbedPane.addTab("Templates", templatesPanel);
    }

    private GridConstraints getConstraints(int row, int column) {
        return new GridConstraints(row, column, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane = new JTabbedPane();
        panel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        generalPanel = new JPanel();
        generalPanel.setLayout(new GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("General", generalPanel);
        generalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        generalModePanel = new JPanel();
        generalModePanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        generalPanel.add(generalModePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generalModePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), null));
        generalModeKeepRadioButton = new JRadioButton();
        generalModeKeepRadioButton.setSelected(false);
        generalModeKeepRadioButton.setText("Keep old javadoc");
        generalModePanel.add(generalModeKeepRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalModeUpdateRadioButton = new JRadioButton();
        generalModeUpdateRadioButton.setSelected(false);
        generalModeUpdateRadioButton.setText("Update old javadoc");
        generalModePanel.add(generalModeUpdateRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalModeReplaceRadioButton = new JRadioButton();
        generalModeReplaceRadioButton.setText("Replace old javadoc");
        generalModePanel.add(generalModeReplaceRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalLevelPanel = new JPanel();
        generalLevelPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        generalPanel.add(generalLevelPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generalLevelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), null));
        generalLevelTypeCheckbox = new JCheckBox();
        generalLevelTypeCheckbox.setSelected(false);
        generalLevelTypeCheckbox.setText("Type");
        generalLevelPanel.add(generalLevelTypeCheckbox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalLevelMethodCheckbox = new JCheckBox();
        generalLevelMethodCheckbox.setSelected(false);
        generalLevelMethodCheckbox.setText("Method");
        generalLevelPanel.add(generalLevelMethodCheckbox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalLevelFieldCheckbox = new JCheckBox();
        generalLevelFieldCheckbox.setSelected(false);
        generalLevelFieldCheckbox.setText("Field");
        generalLevelPanel.add(generalLevelFieldCheckbox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalVisibilityPanel = new JPanel();
        generalVisibilityPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        generalPanel.add(generalVisibilityPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generalVisibilityPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), "Visibility"));
        generalVisibilityPublicCheckbox = new JCheckBox();
        generalVisibilityPublicCheckbox.setSelected(false);
        generalVisibilityPublicCheckbox.setText("Public");
        generalVisibilityPanel.add(generalVisibilityPublicCheckbox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalVisibilityProtectedCheckbox = new JCheckBox();
        generalVisibilityProtectedCheckbox.setSelected(false);
        generalVisibilityProtectedCheckbox.setText("Protected");
        generalVisibilityPanel.add(generalVisibilityProtectedCheckbox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalVisibilityDefaultCheckbox = new JCheckBox();
        generalVisibilityDefaultCheckbox.setSelected(false);
        generalVisibilityDefaultCheckbox.setText("Default");
        generalVisibilityPanel.add(generalVisibilityDefaultCheckbox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalVisibilityPrivateCheckbox = new JCheckBox();
        generalVisibilityPrivateCheckbox.setText("Private");
        generalVisibilityPanel.add(generalVisibilityPrivateCheckbox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalOtherPanel = new JPanel();
        generalOtherPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        generalPanel.add(generalOtherPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generalOtherPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), "Other"));
        generalOtherOverriddenMethodsCheckbox = new JCheckBox();
        generalOtherOverriddenMethodsCheckbox.setText("Comment overridden methods");
        generalOtherPanel.add(generalOtherOverriddenMethodsCheckbox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        generalPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(generalModeKeepRadioButton);
        buttonGroup.add(generalModeUpdateRadioButton);
        buttonGroup.add(generalModeReplaceRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
