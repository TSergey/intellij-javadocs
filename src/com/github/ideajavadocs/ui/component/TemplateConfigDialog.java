package com.github.ideajavadocs.ui.component;

import com.intellij.openapi.ui.DialogWrapper;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TemplateConfigDialog extends DialogWrapper {

    public TemplateConfigDialog() {
        super(true);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new JPanel();  // TODO
    }

}
