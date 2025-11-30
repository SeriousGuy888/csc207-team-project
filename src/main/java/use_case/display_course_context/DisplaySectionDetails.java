package use_case.display_course_context;

import java.util.List;

/**
 * Data transfer object for display section details
 */
public class DisplaySectionDetails {
    private final String sectionName; // e.g., LEC-0101 (Display Name)
    // Scheduling
    private final List<DisplayMeetingTime> meetingTimes; // one or more meetings
    private final String location;      // e.g. "BA 2155" or "Online"
    private final DisplayProfessorDetails professorDetails; // The nested RMP data

    public DisplaySectionDetails(
            String sectionName,
            List<DisplayMeetingTime> meetingTimes,
            String location,
            DisplayProfessorDetails professorDetails) {

        this.sectionName = sectionName;
        this.professorDetails = professorDetails;
        this.meetingTimes = meetingTimes;
        this.location = location;
    }

    // Getters
    public String getSectionName() { return sectionName; }
    public List<DisplayMeetingTime> getMeetingTimes() { return meetingTimes; }
    public String getLocation() { return location; }
    public DisplayProfessorDetails getProfessorDetails() { return professorDetails; }

    @Override
    public String toString() {
        return String.format("%s - Prof: %s",
                sectionName,
                professorDetails.getName());
    }
}