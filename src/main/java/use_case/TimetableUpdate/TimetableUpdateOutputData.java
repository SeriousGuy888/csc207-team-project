package use_case.TimetableUpdate;

import entity.Timetable;

public class TimetableUpdateOutputData {
    private final Timetable timetable;
    private final int tabIndex;
    private final boolean useCaseFailed;

    public TimetableUpdateOutputData(Timetable timetable, int tabIndex, boolean useCaseFailed) {
        this.timetable = timetable;
        this.tabIndex = tabIndex;
        this.useCaseFailed = useCaseFailed;
    }

    public Timetable getTimetable() { return timetable; }
    public int getTabIndex() { return tabIndex; }
    public boolean isUseCaseFailed() { return useCaseFailed; }
}
