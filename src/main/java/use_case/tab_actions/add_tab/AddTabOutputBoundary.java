package use_case.tab_actions.add_tab;

import entity.Workbook;

public interface AddTabOutputBoundary {
    void prepareSuccessView(Workbook workbook);
}
