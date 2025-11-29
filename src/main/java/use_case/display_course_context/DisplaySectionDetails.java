package use_case.display_course_context;

/**
 * Data transfer object for display section details
 */
public class DisplaySectionDetails {
    private final String sectionName; // e.g., LEC0101
    private final String meetingTimes; // e.g., MON, WED 10:00-12:00
    private final String location;     // e.g., BA 1100
    private final DisplayProfessorDetails professorDetails; // The nested RMP data

    public DisplaySectionDetails(
            String sectionName,
            String meetingTimes,
            String location,
            DisplayProfessorDetails professorDetails) {

        this.sectionName = sectionName;
        this.meetingTimes = meetingTimes;
        this.location = location;
        this.professorDetails = professorDetails;
    }

    // Getters
    public String getSectionName() {
        return sectionName;
    }

    public String getMeetingTimes() {
        return meetingTimes;
    }

    public String getLocation() {
        return location;
    }

    public DisplayProfessorDetails getProfessorDetails() {
        return professorDetails;
    }

    @Override
    public String toString() {
        return String.format("%s at %s (%s) - Prof: %s",
                sectionName,
                location,
                meetingTimes,
                professorDetails.getName());
    }
}