package com.github.setial.intellijjavadocs.ui.component;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The type Template config dialog.
 *
 * @author Sergey Timofiychuk
 */
public class TemplateConfigDialog extends DialogWrapper {

    private Entry<String, String> model;

    private JTextField nameField;
    private JTextArea templateField;

    /**
     * Instantiates a new Template config dialog.
     */
    public TemplateConfigDialog() {
        this(null);
    }

    /**
     * Instantiates a new Template config dialog.
     *
     * @param model the model
     */
    public TemplateConfigDialog(Entry<String, String> model) {
        super(true);
        if (model != null) {
            Map<String, String> modelCopy = new HashMap<String, String>();
            modelCopy.put(model.getKey(), model.getValue());
            this.model = modelCopy.entrySet().iterator().next();
        }
        init();
    }

    /**
     * Gets table model.
     *
     * @return the model
     */
    public Entry<String, String> getModel() {
        Map<String, String> model = new HashMap<String, String>();
        model.put(nameField.getText(), templateField.getText());
        return model.entrySet().iterator().next();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));

        nameField = new JTextField();
        if (model != null) {
            nameField.setText(model.getKey());
        }
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBorder(IdeBorderFactory.createTitledBorder("Template regexp", false, new Insets(0, 0, 10, 0)));
        namePanel.add(nameField, BorderLayout.CENTER);

        templateField = new JTextArea();
        if (model != null) {
            templateField.setText(model.getValue());
        }
        JPanel templatePanel = new JPanel(new BorderLayout());
        templatePanel.setBorder(IdeBorderFactory.createTitledBorder("Template content", false, new Insets(0, 0, 0, 0)));
        templatePanel.add(templateField, BorderLayout.CENTER);

        panel.add(namePanel, getConstraints(0, 0));
        panel.add(templatePanel, getConstraints(1, 0));
        return panel;
    }

    private GridConstraints getConstraints(int row, int column) {
        return new GridConstraints(row, column, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false);
    }

}
