package use_case.display_course_context;

/**
 * Interface that the Controller uses to trigger the Use Case.
 */
public interface DisplayCourseDetailsInputBoundary {
    /**
     * Executes the use case: fetches course details, enriches with RMP data, and sends to the presenter.
     * @param inputData Contains the identifier of the course to be displayed.
     */
    void execute(DisplayCourseDetailsInputData inputData);
}
