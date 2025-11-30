package interface_adapter;

import java.util.HashSet;
import java.util.Set;

public class TimetableState {

    private static final int NUM_ROWS = 24;
    private static final int NUM_COLS = 5;
    private static final int MAX_MEETING_BLOCKS = 2;

    // The grid: 24 rows (9am-9pm, 30m slots) x 5 columns (Mon-Fri)
    // Each cell contains a list of blocks (to handle conflicts)
    private final MeetingBlock[][][] firstSemesterGrid;
    private final MeetingBlock[][][] secondSemesterGrid;

    private String timetableName;
    private Set<String> lockedSectionLabels = new HashSet<>();


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

    /**
     * @return a copy of the labels for locked sections (e.g. "CSC207 LEC0101").
     */
    public Set<String> getLockedSectionLabels() {
        return new HashSet<>(lockedSectionLabels);
    }

    /**
     * Replace the current locked section labels with the given set.
     *
     * @param lockedSectionLabels labels for locked sections, e.g. "CSC207 LEC0101"
     */
    public void setLockedSectionLabels(Set<String> lockedSectionLabels) {
        this.lockedSectionLabels = (lockedSectionLabels == null)
                ? new HashSet<>()
                : new HashSet<>(lockedSectionLabels);
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
}
