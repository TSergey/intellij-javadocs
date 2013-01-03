package com.github.ideajavadocs.ui.component;

import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;

import java.awt.event.MouseEvent;
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
        if (e == null ||
                (e instanceof MouseEvent
                        && ((MouseEvent)e).getClickCount() == 1)) {
            return false;
        }
        // TODO

        TemplateConfigDialog dialog = new TemplateConfigDialog();
        dialog.show();
        return false;
    }

    private static class ModelAdapter extends AbstractTableModel implements EditableModel {

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "regexp";
                case 1:
                    return "level";
                case 2:
                    return "preview";
                default:
                    return "";
            }
        }

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
            return true;  // TODO
        }

        @Override
        public int getRowCount() {
            return 1;  // TODO
        }

        @Override
        public int getColumnCount() {
            return 3;  // TODO
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return "1";  // TODO
        }
    }

}
