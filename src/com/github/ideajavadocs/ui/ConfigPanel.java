package com.github.ideajavadocs.ui;

import javax.swing.*;

public class ConfigPanel extends JComponent {

    public ConfigPanel() {
        add(panel);
    }

    private JTabbedPane tabbedPane;
    private JPanel panel;
    private JPanel templatesPanel;
    private JPanel replacementsPanel;
    private JPanel generalPanel;
    private JCheckBox alwaysReplaceJavadocsCheckBox;
}
