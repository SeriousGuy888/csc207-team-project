// use_case/lock_section/LockSectionInputData.java

package use_case.locksections;

public class LockSectionInputData {
    private final int tabIndex;
    private final String courseCode;
    private final String sectionName;
    private final boolean locked;

    public LockSectionInputData(int tabIndex, String courseCode, String sectionName, boolean locked) {
        this.tabIndex = tabIndex;
        this.courseCode = courseCode;
        this.sectionName = sectionName;
        this.locked = locked;
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

    public boolean isLocked() {
        return locked;
    }
}
