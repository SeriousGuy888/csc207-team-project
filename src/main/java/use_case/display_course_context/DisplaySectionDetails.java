package use_case.display_course_context;

/**
 * Data transfer object for display section details
 */
public class DisplaySectionDetails {
    private final String sectionName; // e.g., LEC 01 (Display Name)
    private final DisplayProfessorDetails professorDetails; // The nested RMP data

    public DisplaySectionDetails(
            String sectionName,
            DisplayProfessorDetails professorDetails) {

        this.sectionName = sectionName;
        this.professorDetails = professorDetails;
    }

    // Getters
    public String getSectionName() { return sectionName; }
    public DisplayProfessorDetails getProfessorDetails() { return professorDetails; }

    @Override
    public String toString() {
        // Updated toString() to remove meeting times/location
        return String.format("%s - Prof: %s",
                sectionName,
                professorDetails.getName());
    }
}