package interface_adapter.display_course_context;

import use_case.display_course_context.display_course_details_data_transfer_objects.DisplaySectionDetails;

import java.util.List;

/**
 * An immutable state class holding the information needed by the View for displaying course details.
 */
public class DisplayCourseDetailsState {
    private final String courseId;
    private final String courseTitle;
    private final String courseDescription;
    private final List<DisplaySectionDetails> sectionDetails; // List of formatted section rows
    private final String errorMessage;

    public DisplayCourseDetailsState(
            String courseId,
            String courseTitle,
            String courseDescription,
            List<DisplaySectionDetails> sectionDetails,
            String errorMessage) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.sectionDetails = sectionDetails;
        this.errorMessage = errorMessage;
    }

    // Getters for the View to access the data
    public String getCourseTitle() { return courseTitle; }
    public String getCourseDescription() { return courseDescription; }
    public List<DisplaySectionDetails> getSectionDetails() { return sectionDetails; }
    public String getErrorMessage() { return errorMessage; }
    public String getCourseId() {return courseId; }

    /**
     * Checks if the current state holds an error message.
     * @return true if errorMessage is not null and not empty, false otherwise.
     */
    public boolean isError() {
        return errorMessage != null && !errorMessage.isBlank();
    }

}
