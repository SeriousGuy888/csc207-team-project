package use_case.clear_timetable;

public interface ClearTimetableInputBoundary {
    /**
     * Executes clear timetable.
     * @param tabIndex index for timetable to be cleared
     */
    void execute(int tabIndex);
}
