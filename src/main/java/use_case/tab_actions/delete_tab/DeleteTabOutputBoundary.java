package use_case.tab_actions.delete_tab;

import entity.Workbook;

public interface DeleteTabOutputBoundary {
    /**
     * Prepares the view for the success of the Delete Tab Use Case.
     * @param workbook the workbook whose tab got deleted.
     */
    void prepareSuccessView(Workbook workbook);
}
