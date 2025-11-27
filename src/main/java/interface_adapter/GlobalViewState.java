package interface_adapter;

import java.util.*;

public class GlobalViewState {
    private List<TimetableState> timetableStateList = new ArrayList<>();
    private int selectedTabIndex = 0;

    public List<TimetableState> getTimetableStateList() {
        return timetableStateList;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setTimetableStateList(List<TimetableState> timetableStateList) {
        this.timetableStateList = timetableStateList;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }
}
