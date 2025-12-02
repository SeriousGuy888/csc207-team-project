package interface_adapter;

import use_case.clear_timetable.ClearTimetableInputBoundary;

public class ClearTimetableController {
    private final ClearTimetableInputBoundary interactor;

    public ClearTimetableController(ClearTimetableInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Execute clear timetable.
     * @param tabIndex index of timetable to be cleared
     */
    public void execute(int tabIndex) {
        interactor.execute(tabIndex);
    }
}
