package use_case.tab_actions.rename_tab;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

public class RenameTabInteractor implements RenameTabInputBoundary {
    private final WorkbookDataAccessInterface dataAccess;
    private final RenameTabOutputBoundary presenter;

    public RenameTabInteractor(WorkbookDataAccessInterface dataAccess, RenameTabOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(int tabIndex, String newName) {
        final Workbook workbook = dataAccess.getWorkbook();

        // Validate index to prevent crashes
        if (tabIndex >= 0 && tabIndex < workbook.getTimetables().size()) {
            final Timetable timetable = workbook.getTimetables().get(tabIndex);

            // 1. Change the Name in the Entity
            timetable.setTimetableName(newName);

            // 2. Save the Change
            dataAccess.saveWorkbook(workbook);

            // 3. Update the View
            presenter.prepareSuccessView(workbook);
        }
    }
}
