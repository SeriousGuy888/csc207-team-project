package use_case.tab_actions.add_tab;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

public class AddTabInteractor implements AddTabInputBoundary {
    private final AddTabOutputBoundary presenter;
    // Assume a simple DAO interface exists.
    private final WorkbookDataAccessInterface dataAccess;

    public AddTabInteractor(WorkbookDataAccessInterface dataAccess, AddTabOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        Workbook workbook = dataAccess.getWorkbook();

        // 1. Business Logic: Create new empty timetable
        Timetable newTimetable = new Timetable();
        workbook.addTimetable(newTimetable);

        // 2. Output
        presenter.prepareSuccessView(workbook);
    }
}
