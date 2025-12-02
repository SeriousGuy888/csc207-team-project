package use_case.load_workbook;

import entity.Workbook;

public interface LoadWorkbookGlobalStateOutputBoundary {
    /**
     * Update the global state with a newly loaded workbook.
     * @param loadedWorkbook the workbook that was just successfully loaded
     */
    void prepareSuccessView(Workbook loadedWorkbook);
}
