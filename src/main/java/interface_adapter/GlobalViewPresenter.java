package interface_adapter;

import entity.Meeting;
import entity.Section;
import entity.Timetable;

public class GlobalViewPresenter {
    private GlobalViewModel globalViewModel;

    public GlobalViewPresenter(GlobalViewModel globalViewModel) {
        this.globalViewModel = globalViewModel;
    }

    private TimetableState convertTimetableToState(Timetable timetable) {
        TimetableState state = new TimetableState();
        String[][] firstSemesterGrid = state.getFirstSemesterTimetable();
        String[][] secondSemesterGrid = state.getSecondSemesterTimetable();

        for (Section section : timetable.getSections()) {
            String cellText = section.getCourseOffering().getTitle(); //TODO: format cell text later
//            for (Meeting meeting : section.getMeetings()) {
//
//            }
        }

        return state;
    }
}
