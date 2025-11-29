package use_case.tab_actions.delete_tab;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

public class DeleteTabInteractor implements DeleteTabInputBoundary {
    private final DeleteTabOutputBoundary presenter;
    private final WorkbookDataAccessInterface dataAccess;

    public DeleteTabInteractor(WorkbookDataAccessInterface dataAccess, DeleteTabOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(int tabIndex) {
        final Workbook workbook = dataAccess.getWorkbook();

        // Safety Check
        if (tabIndex >= 0 && tabIndex < workbook.getTimetables().size()) {
            final Timetable toRemove = workbook.getTimetables().get(tabIndex);

            // 1. Business Logic: Remove
            workbook.removeTimetable(toRemove);
            dataAccess.saveWorkbook(workbook);

            // 2. Output
            presenter.prepareSuccessView(workbook);
        }
    }
}