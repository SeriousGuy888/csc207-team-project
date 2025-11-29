package data_access.workbook_instance;

import entity.Workbook;

public interface CurrentWorkbook {
    /**
     * Retrieves the current workbook (project file).
     * @return the active Workbook object.
     */
    Workbook getWorkbook();
}
