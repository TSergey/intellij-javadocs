package com.github.ideajavadocs.ui.component;

import com.intellij.openapi.ui.DialogWrapper;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TemplateConfigDialog extends DialogWrapper {

    public TemplateConfigDialog() {
        super(true);
        init();
    }



    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        // TODO
        return new JPanel(new BorderLayout());
    }

}
