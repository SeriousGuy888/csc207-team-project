package use_case.tab_actions.rename_tab;

import entity.Workbook;

public interface RenameTabOutputBoundary {
    void prepareSuccessView(Workbook workbook);
}
