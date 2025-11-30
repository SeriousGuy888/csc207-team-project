package use_case.tab_actions.add_tab;

import entity.Workbook;

public interface AddTabOutputBoundary {
    /**
     * Prepares the view for the success of the Add Tab Use Case.
     * @param workbook the workbook that was added.
     */
    void prepareSuccessView(Workbook workbook);
}
