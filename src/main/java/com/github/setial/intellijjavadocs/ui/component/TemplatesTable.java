package com.github.setial.intellijjavadocs.ui.component;

import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The type Templates table.
 *
 * @author Sergey Timofiychuk
 */
public class TemplatesTable extends JBTable {

    private List<Entry<String, String>> settings;

    /**
     * Instantiates a new Templates table.
     *
     * @param model the model
     */
    @SuppressWarnings("unchecked")
    public TemplatesTable(Map<String, String> model) {
        setStriped(true);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        settings = new LinkedList<Entry<String, String>>();
        CollectionUtils.addAll(settings, model.entrySet().toArray(new Entry[model.entrySet().size()]));
        setModel(new TableModel());
        Enumeration<TableColumn> columns = getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            columns.nextElement().setCellRenderer(new FieldRenderer());
        }
    }

    /**
     * Gets settings model.
     *
     * @return clone of the original settings model
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getSettings() {
        return MapUtils.putAll(new LinkedHashMap(), settings.toArray());
    }

    /**
     * Sets settings model.
     *
     * @param model the model
     */
    @SuppressWarnings("unchecked")
    public void setSettingsModel(Map<String, String> model) {
        settings.clear();
        CollectionUtils.addAll(settings, model.entrySet().toArray(new Entry[model.entrySet().size()]));
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
        TemplateConfigDialog dialog = new TemplateConfigDialog(settings.get(row));
        dialog.show();
        if (dialog.isOK()) {
            settings.set(row, dialog.getModel());
        }
        return false;
    }

    private class TableModel extends AbstractTableModel implements EditableModel {

        private List<String> columnNames;

        /**
         * Instantiates a new Table model.
         */
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
                settings.add(dialog.getModel());
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

    private static class FieldRenderer extends JLabel implements TableCellRenderer {

        /**
         * Instantiates a new Field renderer.
         */
        public FieldRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            setBorder(hasFocus ?
                    UIUtil.getTableFocusCellHighlightBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));
            setText(value == null ? "" : value.toString());
            return this;
        }

    }


}
