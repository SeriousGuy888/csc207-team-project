package use_case.display_course_context;

public class DisplayCourseDetailsInputData {
    private final String courseOfferingId; // The single unique ID needed for input

    /**
     * Constructs the Input Data required to trigger the Display Course Details Use Case.
     * @param courseOfferingId The unique identifier of the course offering
     * (e.g., "CSC207H1-F-20259") selected by the user.
     */
    public DisplayCourseDetailsInputData(String courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    /**
     * @return The unique identifier of the specific course instance the user clicked on.
     */
    public String getCourseOfferingId() {
        return courseOfferingId;
    }
}