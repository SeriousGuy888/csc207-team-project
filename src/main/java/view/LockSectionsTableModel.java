package view;

import entity.Section;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Table model for the "Locked Sections" table:
 * Column 0: section label (e.g. "CSC207 LEC0101")
 * Column 1: locked checkbox (Boolean)
 */
public class LockSectionsTableModel extends AbstractTableModel {

    private final List<Section> sections = new ArrayList<>();
    private final Set<String> lockedLabels = new HashSet<>();

    private static final int COL_SECTION = 0;
    private static final int COL_LOCKED = 1;

    @Override
    public int getRowCount() {
        return sections.size();
    }

    @Override
    public int getColumnCount() {
        return 2; // "Section" | "Locked"
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case COL_SECTION:
                return "Section";
            case COL_LOCKED:
                return "Locked";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case COL_SECTION:
                return String.class;
            case COL_LOCKED:
                return Boolean.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COL_LOCKED; // only the checkbox column
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Section section = sections.get(rowIndex);
        String label = buildLabel(section);

        switch (columnIndex) {
            case COL_SECTION:
                return label;
            case COL_LOCKED:
                return lockedLabels.contains(label);
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != COL_LOCKED || rowIndex < 0 || rowIndex >= sections.size()) {
            return;
        }

        boolean locked = (Boolean) aValue;
        Section section = sections.get(rowIndex);
        String label = buildLabel(section);

        if (locked) {
            lockedLabels.add(label);
        } else {
            lockedLabels.remove(label);
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Rebuilds the rows when the timetable changes.
     *
     * @param newSections     the sections currently in the timetable
     * @param labelsFromState labels that should be checked, e.g. from TimetableState.getLockedSectionLabels()
     */
    public void updateSections(List<Section> newSections, Set<String> labelsFromState) {
        sections.clear();
        sections.addAll(newSections);

        lockedLabels.clear();
        if (labelsFromState != null) {
            for (Section s : newSections) {
                String label = buildLabel(s);
                if (labelsFromState.contains(label)) {
                    lockedLabels.add(label);
                }
            }
        }

        fireTableDataChanged();
    }

    /**
     * @return the set of Section entities that are currently marked as locked.
     */
    public Set<Section> getLockedSections() {
        Set<Section> result = new HashSet<>();
        for (Section section : sections) {
            String label = buildLabel(section);
            if (lockedLabels.contains(label)) {
                result.add(section);
            }
        }
        return result;
    }

    /**
     * Must match the presenter + TimetableState label format.
     * Example: "CSC207 LEC0101"
     */
    private String buildLabel(Section section) {
        String course = section.getCourseOffering().getCourseCode().toString();
        String sect = section.getSectionName();
        return course + " " + sect;
    }
}
