package use_case.tab_actions.rename_tab;

import entity.Workbook;

public interface RenameTabOutputBoundary {
    /**
     * Prepares the view for the success of the Rename Tab Use Case.
     * @param workbook the workbook whose tab got renamed.
     */
    void prepareSuccessView(Workbook workbook);
}
