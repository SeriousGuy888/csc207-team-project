package use_case.autogen;

import java.util.Set;

import entity.CourseCode;
import entity.Section;
import entity.WeeklyOccupancy;

public class AutogenInputData {
    private final Set<CourseCode> selectedCourses;
    private final Set<Section> lockedSections;
    private final WeeklyOccupancy blockedTimes;

    public AutogenInputData(Set<CourseCode> selectedCourses,
                            Set<Section> lockedSections,
                            WeeklyOccupancy blockedTimes) {
        this.selectedCourses = selectedCourses;
        this.lockedSections = lockedSections;
        this.blockedTimes = blockedTimes;
    }

    public Set<CourseCode> getSelectedCourses() {
        return selectedCourses;
    }

    public Set<Section> getLockedSections() {
        return lockedSections;
    }

    public WeeklyOccupancy getBlockedTimes() {
        return blockedTimes;
    }
}
