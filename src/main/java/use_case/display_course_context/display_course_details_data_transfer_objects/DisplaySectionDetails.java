package use_case.display_course_context.display_course_details_data_transfer_objects;

import java.util.List;

/**
 * Data transfer object for display section details
 */
public class DisplaySectionDetails {
    private final String sectionName; // e.g., LEC-0101 (Display Name)
    // Scheduling
    private final List<DisplayMeetingDetails> meetingTimes; // one or more meetings
    private final DisplayProfessorDetails professorDetails; // The nested RMP data

    public DisplaySectionDetails(
            String sectionName,
            List<DisplayMeetingDetails> meetingTimes,
            DisplayProfessorDetails professorDetails) {

        this.sectionName = sectionName;
        this.professorDetails = professorDetails;
        this.meetingTimes = meetingTimes;
    }

    // Getters
    public String getSectionName() { return sectionName; }
    public List<DisplayMeetingDetails> getMeetingTimes() { return meetingTimes; }
    public DisplayProfessorDetails getProfessorDetails() { return professorDetails; }

    @Override
    public String toString() {
        return String.format("%s - Prof: %s",
                sectionName,
                professorDetails.getName());
    }
}