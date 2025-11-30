package interface_adapter.display_course_context;

import java.util.List;

/**
 * An immutable state class holding the information needed by the View for displaying course details.
 */
public class DisplayCourseDetailsState {
    private final String courseTitle;
    private final String courseDescription;
    private final List<SectionDisplayData> sectionDetails; // List of formatted section rows
    private final String errorMessage;

    public DisplayCourseDetailsState(
            String courseTitle,
            String courseDescription,
            List<SectionDisplayData> sectionDetails,
            String errorMessage) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.sectionDetails = sectionDetails;
        this.errorMessage = errorMessage;
    }

    // Getters for the View to access the data
    public String getCourseTitle() { return courseTitle; }
    public String getCourseDescription() { return courseDescription; }
    public List<SectionDisplayData> getSectionDetails() { return sectionDetails; }
    public String getErrorMessage() { return errorMessage; }
}
