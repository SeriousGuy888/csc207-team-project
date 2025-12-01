package interface_adapter;

import java.util.List;
import java.util.ArrayList;

public class TimetableState {

    private static final int NUM_ROWS = 24;
    private static final int NUM_COLS = 5;
    private static final int MAX_MEETING_BLOCKS = 2;

    // The grid: 24 rows (9am-9pm, 30m slots) x 5 columns (Mon-Fri)
    // Each cell contains a list of blocks (to handle conflicts)
    private final MeetingBlock[][][] firstSemesterGrid;
    private final MeetingBlock[][][] secondSemesterGrid;
    private List<SelectedSectionRow> selectedSections = new ArrayList<>();



    private String timetableName;

    public TimetableState() {
        // Initialize empty grids
        firstSemesterGrid = new MeetingBlock[NUM_ROWS][NUM_COLS][MAX_MEETING_BLOCKS];
        secondSemesterGrid = new MeetingBlock[NUM_ROWS][NUM_COLS][MAX_MEETING_BLOCKS];
    }

    public MeetingBlock[][][] getFirstSemesterGrid() {
        return firstSemesterGrid;
    }

    public MeetingBlock[][][] getSecondSemesterGrid() {
        return secondSemesterGrid;
    }

    public String getTimetableName() {
        return timetableName;
    }

    public void setTimetableName(String timetableName) {
        this.timetableName = timetableName;
    }

    // Inner Class: DTO for UI display
    public static class MeetingBlock {
        private final String courseCode;
        private final String sectionInfo;
        private final int startRow;
        private final boolean isConflict;

        public MeetingBlock(String courseCode, String sectionInfo, int startRow, boolean isConflict) {
            this.courseCode = courseCode;
            this.sectionInfo = sectionInfo;
            this.startRow = startRow;
            this.isConflict = isConflict;
        }

        public String getDisplayText() {
            // HTML formatting for multiline text in JLabel
            return "<html><center>" + courseCode + "<br>" + sectionInfo + "</center></html>";
        }

        public int getStartRow() {
            return startRow;
        }

        public boolean isConflict() {
            return isConflict;
        }
    }
    public List<SelectedSectionRow> getSelectedSections() {
        return selectedSections;
    }

    public void setSelectedSections(List<SelectedSectionRow> selectedSections) {
        this.selectedSections = selectedSections;
    }

    public static class SelectedSectionRow {
        private final String courseCode;
        private final String sectionName;
        private final String teachingMethod;
        private boolean locked;  // <-- remove final

        public SelectedSectionRow(String courseCode,
                                  String sectionName,
                                  String teachingMethod,
                                  boolean locked) {
            this.courseCode = courseCode;
            this.sectionName = sectionName;
            this.teachingMethod = teachingMethod;
            this.locked = locked;
        }

        public String getCourseCode() { return courseCode; }
        public String getSectionName() { return sectionName; }
        public String getTeachingMethod() { return teachingMethod; }
        public boolean isLocked() { return locked; }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }
    }

}
