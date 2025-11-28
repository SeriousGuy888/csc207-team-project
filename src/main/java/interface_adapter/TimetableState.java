package interface_adapter;

import java.util.ArrayList;
import java.util.List;

public class TimetableState {

    // The grid: 24 rows (9am-9pm, 30m slots) x 5 columns (Mon-Fri)
    // Each cell contains a list of blocks (to handle conflicts)
    private final List<MeetingBlock>[][] firstSemesterGrid;
    private final List<MeetingBlock>[][] secondSemesterGrid;

    public TimetableState() {
        // Initialize empty grids
        firstSemesterGrid = new List[24][5];
        secondSemesterGrid = new List[24][5];
        for (int row = 0; row < 24; row++) {
            for (int col = 0; col < 5; col++) {
                firstSemesterGrid[row][col] = new ArrayList<>();
                secondSemesterGrid[row][col] = new ArrayList<>();
            }
        }
    }

    public List<MeetingBlock>[][] getFirstSemesterGrid() {
        return firstSemesterGrid;
    }

    public List<MeetingBlock>[][] getSecondSemesterGrid() {
        return secondSemesterGrid;
    }

    // Inner Class: DTO for UI display
    public static class MeetingBlock {
        private final String courseCode;   // "CSC207"
        private final String sectionInfo;  // "LEC0101"
        private final boolean isConflict;

        public MeetingBlock(String courseCode, String sectionInfo, boolean isConflict) {
            this.courseCode = courseCode;
            this.sectionInfo = sectionInfo;
            this.isConflict = isConflict;
        }

        public String getDisplayText() {
            // HTML formatting for multiline text in JLabel
            return "<html><center>" + courseCode + "<br>" + sectionInfo + "</center></html>";
        }

        public boolean isConflict() { return isConflict; }
    }
}