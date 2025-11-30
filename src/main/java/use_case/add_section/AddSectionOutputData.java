package use_case.add_section;

import entity.Timetable;
import java.util.List;

// Contains the updated Timetable so presenter can update GlobalViewState.
public class AddSectionOutputData {
    private final Timetable timetable;
    private final int tabIndex;
    private final String courseCode;
    private final String sectionName;
    private final List<String> conflictingSections;

    public AddSectionOutputData(Timetable timetable, int selectedTabIndex, String courseCode,
                                 String sectionName, List<String> conflictingSections) {
        this.timetable = timetable;
        this.tabIndex = selectedTabIndex;
        this.courseCode = courseCode;
        this.sectionName = sectionName;
        this.conflictingSections = conflictingSections;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSectionName() {
        return sectionName;
    }

    public List<String> getConflictingSections() {
        return conflictingSections;
    }

    public boolean hasConflicts() {
        return !conflictingSections.isEmpty();
    }
}