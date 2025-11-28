package data_access;

import entity.Workbook;

/**
 * Holds entity classes that represent the current state of the UI.
 * Doesn't represent the <em>entirety</em> of the UI state:
 * it contains only the parts that concern the file the user is editing.
 */
public class GuiStateDataAccessObject {
    private Workbook activeWorkbook;

    public Workbook getActiveWorkbook() {
        return activeWorkbook;
    }

    public void setActiveWorkbook(Workbook activeWorkbook) {
        this.activeWorkbook = activeWorkbook;
    }
}
