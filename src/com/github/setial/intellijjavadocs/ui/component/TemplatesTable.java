package com.github.setial.intellijjavadocs.ui.component;

import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;
import org.apache.commons.collections.MapUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

public class TemplatesTable extends JBTable {

    private List<Entry<String, String>> settings;

    @SuppressWarnings("unchecked")
    public TemplatesTable(Map<String, String> model) {
        setModel(new TableModel());
        setStriped(true);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        settings = new LinkedList<Entry<String, String>>();
        for (Entry<String, String> entry :model.entrySet().toArray(new Entry[model.entrySet().size()])) {
            settings.add(entry);
        }
    }

    /**
     *
     * @return clone of the original settings model
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getSettings() {
        return MapUtils.putAll(new LinkedHashMap(), settings.toArray());
    }

    @SuppressWarnings("unchecked")
    public void setSettingsModel(Map<String, String> model) {
        settings.clear();
        for (Entry<String, String> entry : model.entrySet().toArray(new Entry[model.entrySet().size()])) {
            settings.add(entry);
        }
        ((TableModel) getModel()).fireTableDataChanged();
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        if (e == null) {
            return false;
        }
        if (e instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) e;
            if (event.getClickCount() == 1) {
                return false;
            }
        }
        // TODO

        TemplateConfigDialog dialog = new TemplateConfigDialog();
        dialog.show();
        return false;
    }

    private class TableModel extends AbstractTableModel implements EditableModel {

        private List<String> columnNames;

        public TableModel() {
            columnNames = new LinkedList<String>();
            columnNames.add("Regular expression");
            columnNames.add("Preview");
        }

        @Override
        public String getColumnName(int column) {
            return columnNames.get(column);
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
            settings.remove(index);
        }

        @Override
        public void exchangeRows(int oldIndex, int newIndex) {
            Entry<String, String> oldItem = settings.get(oldIndex);
            settings.set(oldIndex, settings.get(newIndex));
            settings.set(newIndex, oldItem);
        }

        @Override
        public boolean canExchangeRows(int oldIndex, int newIndex) {
            return true;
        }

        @Override
        public int getRowCount() {
            return settings.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex == 0 ? settings.get(rowIndex).getKey() : settings.get(rowIndex).getValue();
        }

    }

}
