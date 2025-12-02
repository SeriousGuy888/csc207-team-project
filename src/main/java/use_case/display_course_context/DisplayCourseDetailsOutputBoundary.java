package use_case.display_course_context;

/**
 * Interface that the Interactor uses to signal the result of the use case execution.
 */
public interface DisplayCourseDetailsOutputBoundary {
    /**
     * Called when course details are successfully fetched and prepared.
     * @param outputData The final, display-ready data structure.
     */
    void prepareSuccessView(DisplayCourseDetailsOutputData outputData);

    /**
     * Called when the use case fails (e.g., course ID is not found).
     * @param error A descriptive error message to show the user.
     */
    void prepareFailView(String failedCourseId, String error);
}