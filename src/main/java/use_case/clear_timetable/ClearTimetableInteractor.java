package use_case.clear_timetable;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputData;

public class ClearTimetableInteractor implements ClearTimetableInputBoundary {
    private final WorkbookDataAccessInterface dataAccess;
    private final TimetableUpdateOutputBoundary presenter;

    public ClearTimetableInteractor(WorkbookDataAccessInterface dataAccess,
                                    TimetableUpdateOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(int tabIndex) {
        final Workbook workbook = dataAccess.getWorkbook();

        if (tabIndex >= 0 && tabIndex < workbook.getTimetables().size()) {
            final Timetable timetable = workbook.getTimetables().get(tabIndex);

            // 1. Business Logic: Clear the entity
            timetable.clear();

            // 2. Save State
            dataAccess.saveWorkbook(workbook);

            // 3. Update View (Reuse the existing Update Output Data)
            final TimetableUpdateOutputData outputData = new TimetableUpdateOutputData(timetable, tabIndex);
            presenter.prepareSuccessView(outputData);
        }
    }
}
