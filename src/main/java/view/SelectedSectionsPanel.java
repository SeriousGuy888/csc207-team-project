
package view;

import interface_adapter.TimetableState.SelectedSectionRow;
import interface_adapter.locksections.LockSectionController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectedSectionsPanel extends JPanel {

    private final JTable table;
    private final SelectedSectionsTableModel model;
    private LockSectionController lockSectionController;
    private int tabIndex = 0;

    public SelectedSectionsPanel() {
        this.model = new SelectedSectionsTableModel();
        this.table = new JTable(model);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setFillsViewportHeight(true);
    }

    public void setLockSectionController(LockSectionController controller, int tabIndex) {
        this.lockSectionController = controller;
        this.tabIndex = tabIndex;
        model.setLockSectionCallback((courseCode, sectionName, locked) -> {
            if (lockSectionController != null) {
                lockSectionController.toggleLock(tabIndex, courseCode, sectionName, locked);
            }
        });
    }

    public void update(List<SelectedSectionRow> rows) {
        model.setRows(rows);
    }

    // Expose root if needed
    public JComponent getRootComponent() {
        return this;
    }

    // ---------------- TableModel ----------------

    private static class SelectedSectionsTableModel extends AbstractTableModel {

        private final String[] columns = {"Course", "Section", "Method", "Locked"};
        private List<SelectedSectionRow> rows = new ArrayList<>();

        interface LockSectionCallback {
            void lockChanged(String courseCode, String sectionName, boolean locked);
        }

        private LockSectionCallback callback;

        public void setLockSectionCallback(LockSectionCallback callback) {
            this.callback = callback;
        }

        public void setRows(List<SelectedSectionRow> rows) {
            this.rows = new ArrayList<>(rows);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return Boolean.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3; // only the "Locked" checkbox
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SelectedSectionRow row = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return row.getCourseCode();
                case 1:
                    return row.getSectionName();
                case 2:
                    return row.getTeachingMethod();
                case 3:
                    return row.isLocked();
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3 && rowIndex >= 0 && rowIndex < rows.size()) {
                boolean locked = (Boolean) aValue;
                SelectedSectionRow row = rows.get(rowIndex);

                // Update local copy
                rows.set(rowIndex, new SelectedSectionRow(
                        row.getCourseCode(),
                        row.getSectionName(),
                        row.getTeachingMethod(),
                        locked
                ));
                fireTableRowsUpdated(rowIndex, rowIndex);

                // Notify use case
                if (callback != null) {
                    callback.lockChanged(row.getCourseCode(), row.getSectionName(), locked);
                }
            }
        }
    }
}
