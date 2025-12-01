package interface_adapter;

public class TimetableState {

    private static final int NUM_ROWS = 24;
    private static final int NUM_COLS = 5;
    private static final int MAX_MEETING_BLOCKS = 2;

    // The grid: 24 rows (9am-9pm, 30m slots) x 5 columns (Mon-Fri)
    // Each cell contains a list of blocks (to handle conflicts)
    private final MeetingBlock[][][] firstSemesterGrid;
    private final MeetingBlock[][][] secondSemesterGrid;

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
        public static final int SECONDS_PER_MINUTE = 60;

        private final String courseCode;
        private final String sectionInfo;
        private final String location;
        private final int startRow;
        private final boolean isConflict;
        private String walktimeMessage;

        public MeetingBlock(String courseCode, String sectionInfo, String location, int startRow, boolean isConflict) {
            this.courseCode = courseCode;
            this.sectionInfo = sectionInfo;
            this.location = location;
            this.startRow = startRow;
            this.isConflict = isConflict;
        }

        public String getDisplayText() {
            // HTML formatting for multiline text in JLabel
            return "<html><center>" + courseCode + "<br>" + sectionInfo + "<br>" + location + "</center></html>";
        }

        public int getStartRow() {
            return startRow;
        }

        public boolean isConflict() {
            return isConflict;
        }

        public String getBuildingCode() {
            return location.substring(0, 2);
        }

        public String getWalktimeMessage() {
            return walktimeMessage;
        }

        public void setWalktimeMessage(double durationInMinutes) {
            if (durationInMinutes >= 1) {
                walktimeMessage = (int) Math.ceil(durationInMinutes) + "-minute walk";
            }
            else {
                walktimeMessage = "Less than 1 minute walk";
            }
        }
    }
}
