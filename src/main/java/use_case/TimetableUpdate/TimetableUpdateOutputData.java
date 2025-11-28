package use_case.TimetableUpdate;

import entity.Timetable;

public class TimetableUpdateOutputData {
    private final Timetable timetable;
    private final int tabIndex;

    public TimetableUpdateOutputData(Timetable timetable, int tabIndex) {
        this.timetable = timetable;
        this.tabIndex = tabIndex;
    }

    public Timetable getTimetable() { return timetable; }
    public int getTabIndex() { return tabIndex; }
}
