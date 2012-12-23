package com.github.ideajavadocs.ui;

import com.github.ideajavadocs.model.settings.JavaDocSettings;
import com.github.ideajavadocs.model.settings.Level;
import com.github.ideajavadocs.model.settings.Mode;
import com.github.ideajavadocs.model.settings.Visibility;

import javax.swing.*;

public class ConfigPanel {

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
    private JPanel templatesPanel;
    private JPanel replacementsPanel;
    private JPanel generalModePanel;
    private JPanel generalLevelPanel;
    private JPanel generalVisibilityPanel;
    private JPanel generalOtherPanel;

    public ConfigPanel(JavaDocSettings settings) {
        this.settings = settings;
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean isModified() {
        boolean result = false;
        if (generalModeKeepRadioButton.isSelected()) {
            result = settings.getMode() != Mode.KEEP;
        } else if (generalModeUpdateRadioButton.isSelected()) {
            result = settings.getMode() != Mode.UPDATE;
        } else if (generalModeReplaceRadioButton.isSelected()) {
            result = settings.getMode() != Mode.REPLACE;
        }
        result = result || isCheckboxModified(generalLevelTypeCheckbox, settings.getLevels().contains(Level.TYPE));
        result = result || isCheckboxModified(generalLevelMethodCheckbox, settings.getLevels().contains(Level.METHOD));
        result = result || isCheckboxModified(generalLevelFieldCheckbox, settings.getLevels().contains(Level.FIELD));
        result = result || isCheckboxModified(
                generalVisibilityPublicCheckbox, settings.getVisibilities().contains(Visibility.PUBLIC));
        result = result || isCheckboxModified(
                generalVisibilityProtectedCheckbox, settings.getVisibilities().contains(Visibility.PROTECTED));
        result = result || isCheckboxModified(
                generalVisibilityDefaultCheckbox, settings.getVisibilities().contains(Visibility.DEFAULT));
        result = result || isCheckboxModified(
                generalVisibilityPrivateCheckbox, settings.getVisibilities().contains(Visibility.PRIVATE));
        result = result || isCheckboxModified(generalOtherOverriddenMethodsCheckbox, settings.isOverriddenMethods());
        return result;
    }

    private boolean isCheckboxModified(JCheckBox checkbox, boolean oldValue) {
        return checkbox.isSelected() != oldValue;
    }

    public void apply() {
        if (generalModeKeepRadioButton.isSelected()) {
            settings.setMode(Mode.KEEP);
        } else if (generalModeUpdateRadioButton.isSelected()) {
            settings.setMode(Mode.UPDATE);
        } else if (generalModeReplaceRadioButton.isSelected()) {
            settings.setMode(Mode.REPLACE);
        }

        settings.getLevels().clear();
        if (generalLevelTypeCheckbox.isSelected()) {
            settings.getLevels().add(Level.TYPE);
        }
        if (generalLevelMethodCheckbox.isSelected()) {
            settings.getLevels().add(Level.METHOD);
        }
        if (generalLevelFieldCheckbox.isSelected()) {
            settings.getLevels().add(Level.FIELD);
        }

        settings.getVisibilities().clear();
        if (generalVisibilityPublicCheckbox.isSelected()) {
            settings.getVisibilities().add(Visibility.PUBLIC);
        }
        if (generalVisibilityProtectedCheckbox.isSelected()) {
            settings.getVisibilities().add(Visibility.PROTECTED);
        }
        if (generalVisibilityDefaultCheckbox.isSelected()) {
            settings.getVisibilities().add(Visibility.DEFAULT);
        }
        if (generalVisibilityPrivateCheckbox.isSelected()) {
            settings.getVisibilities().add(Visibility.PRIVATE);
        }

        settings.setOverriddenMethods(generalOtherOverriddenMethodsCheckbox.isSelected());
    }

    public void reset() {
        switch (settings.getMode()) {
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
        for (Level level : settings.getLevels()) {
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
        for (Visibility visibility : settings.getVisibilities()) {
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
        generalOtherOverriddenMethodsCheckbox.setSelected(settings.isOverriddenMethods());
    }

    public void disposeUIResources() {
    }

}
