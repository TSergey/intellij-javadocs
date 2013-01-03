package com.github.ideajavadocs.ui.component;

import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;

import java.util.EventObject;

import javax.swing.table.AbstractTableModel;

public class TemplatesTable extends JBTable {

    public TemplatesTable() {
        super(new ModelAdapter());
        setStriped(true);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        // TODO
        TemplateConfigDialog dialog = new TemplateConfigDialog();
        dialog.show();
        return false;
    }

    private static class ModelAdapter extends AbstractTableModel implements EditableModel {

        @Override
        public void addRow() {
            TemplateConfigDialog dialog = new TemplateConfigDialog();
            dialog.show();
            if (dialog.isOK()) {
                // TODO
            }
        }

        @Override
        public void removeRow(int index) {
            // TODO
        }

        @Override
        public void exchangeRows(int oldIndex, int newIndex) {
            // TODO
        }

        @Override
        public boolean canExchangeRows(int oldIndex, int newIndex) {
            return false;  // TODO
        }

        @Override
        public int getRowCount() {
            return 3;  // TODO
        }

        @Override
        public int getColumnCount() {
            return 0;  // TODO
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;  // TODO
        }
    }

}
