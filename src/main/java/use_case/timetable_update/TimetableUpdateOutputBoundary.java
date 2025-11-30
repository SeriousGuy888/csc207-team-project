package use_case.timetable_update;

public interface TimetableUpdateOutputBoundary {
    /**
     * Prepares the view for the success of the timetable update.
     * @param timetableOutputData the updated timetable data
     */
    void prepareSuccessView(TimetableUpdateOutputData timetableOutputData);

    /**
     * Prepares the view for the failure of the timetable update.
     * @param error the error message
     */
    void prepareFailView(String error);
}
