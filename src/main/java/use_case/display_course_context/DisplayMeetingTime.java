package use_case.display_course_context;

/**
 * Data transfer object for displaying simple data type meeting details for use case display course context.
 */
public class DisplayMeetingTime {
    private final String dayOfWeek;   // e.g. "Mon", "Tue"
    private final String startTime;   // e.g. "10:00"
    private final String endTime;     // e.g. "11:00"

    public DisplayMeetingTime(String dayOfWeek, String startTime, String endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}
