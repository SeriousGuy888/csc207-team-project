package use_case.TimetableUpdate;

import entity.Timetable;

public interface TimetableUpdateOutputBoundary {
    void prepareSuccessView(TimetableUpdateOutputData timetableOutputData);
    void prepareFailView(String error);
}
