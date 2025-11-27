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
        String[][][] firstSemesterGrid = state.getFirstSemesterTimetable();
        String[][][] secondSemesterGrid = state.getSecondSemesterTimetable();

        for (Section section : timetable.getSections()) {
            String cellText = section.getCourseOffering().getTitle(); //TODO: format cell text later
            for (Meeting meeting : section.getMeetings()) {
                if (meeting.getSemester() == Meeting.Semester.FIRST) {
                    fillGridWithMeeting(firstSemesterGrid, meeting, cellText);
                }
                else if (meeting.getSemester() == Meeting.Semester.SECOND) {
                    fillGridWithMeeting(secondSemesterGrid, meeting, cellText);
                }
            }
        }

        return state;
    }

    private void fillGridWithMeeting(String[][][] grid, Meeting meeting, String cellText) {
        int START_HOUR_INDEX = 18;
        int END_HOUR_INDEX = 42;

        for (int day = 0; day < 5; day++) {
            for (int slot = START_HOUR_INDEX; slot < END_HOUR_INDEX; slot++) {
                if (meeting.checkOccupancy(day, slot)) {
                    int row = slot - START_HOUR_INDEX;
                    int col = day;
                    if (grid[row][col] != null) {
                        for (int i = 0; i < grid[row][col].length; i++) {
                            if (grid[row][col][i] == null) {
                                grid[row][col][i] = cellText;
                            }
                        }
                    }
                    else{
                        grid[row][col] = new String[meeting.getNumConflicts() + 1];
                        grid[row][col][0] = cellText;
                    }
                }
            }
        }
    }
}
