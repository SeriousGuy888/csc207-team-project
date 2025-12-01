package interface_adapter.display_course_context;

import use_case.display_course_context.DisplayCourseDetailsInputBoundary;
import use_case.display_course_context.DisplayCourseDetailsInputData;

/**
 * Handles the user action (dropdown click) that triggers the Display Course Details Use Case.
 */
public class DisplayCourseDetailsController {

    // Dependency on the Use Case Interactor (Input Boundary)
    final DisplayCourseDetailsInputBoundary displayCourseInteractor;

    public DisplayCourseDetailsController(DisplayCourseDetailsInputBoundary displayCourseInteractor) {
        this.displayCourseInteractor = displayCourseInteractor;
    }

    /**
     * Called when the user clicks the dropdown button next to a course search result.
     * * @param courseOfferingId The unique ID of the course instance (e.g., "CSC207H1-F-20259").
     */
    public void execute(String courseOfferingId) {

        // 1. Package the raw input into the Input Data DTO
        DisplayCourseDetailsInputData inputData =
                new DisplayCourseDetailsInputData(courseOfferingId);

        // 2. Pass the DTO to the Interactor to start the use case logic
        displayCourseInteractor.execute(inputData);
    }
}