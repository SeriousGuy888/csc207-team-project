package use_case.tab_actions.delete_tab;

import entity.Workbook;

public interface DeleteTabOutputBoundary {
    void prepareDeleteTabSuccessView(Workbook workbook, int tabIndex);
}
