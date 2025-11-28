package interface_adapter;

import java.util.ArrayList;
import java.util.List;

public class TimetableState {

    // The grid: 24 rows (9am-9pm, 30m slots) x 5 columns (Mon-Fri)
    // Each cell contains a list of blocks (to handle conflicts)
    private final MeetingBlock[][][] firstSemesterGrid;
    private final MeetingBlock[][][] secondSemesterGrid;

    public TimetableState() {
        // Initialize empty grids
        firstSemesterGrid = new MeetingBlock[24][5][2];
        secondSemesterGrid = new MeetingBlock[24][5][2];
    }

    public MeetingBlock[][][] getFirstSemesterGrid() {
        return firstSemesterGrid;
    }

    public MeetingBlock[][][] getSecondSemesterGrid() {
        return secondSemesterGrid;
    }

    // Inner Class: DTO for UI display
    public static class MeetingBlock {
        private final String courseCode;   // "CSC207"
        private final String sectionInfo;  // "LEC0101"
        private final int startRow;  // only print info on first block
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

        public int getStartRow() { return startRow; }

        public boolean isConflict() { return isConflict; }
    }
}