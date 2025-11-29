package use_case;

import entity.Workbook;

public interface WorkbookDataAccessInterface {
    /**
     * Retrieves the current workbook (project file).
     * @return the active Workbook object.
     */
    Workbook getWorkbook();

    /**
     * Saves the workbook.
     * Call this after making modifications (add/remove tabs, add courses) to ensure persistence.
     * * @param workbook the workbook to save.
     */
    void saveWorkbook(Workbook workbook);
}
