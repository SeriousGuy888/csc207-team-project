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
        final Workbook workbook = dataAccess.getWorkbook();

        // 1. Generate a smart default name based on existing count
        final int nextNumber = workbook.getTimetables().size() + 1;
        final String name = "Timetable " + nextNumber;

        // 2. Create and Name the Entity
        final Timetable newTimetable = new Timetable();
        newTimetable.setTimetableName(name);

        workbook.addTimetable(newTimetable);
        dataAccess.saveWorkbook(workbook);

        // 3. Output
        presenter.prepareSuccessView(workbook);
    }
}
