package use_case.display_course_context;


import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayCourseDetails;

/**
 * Contains the final course details structure for the Presenter to format.
 */
public class DisplayCourseDetailsOutputData {
    private final DisplayCourseDetails courseDetails;

    public DisplayCourseDetailsOutputData(DisplayCourseDetails courseDetails) {
        this.courseDetails = courseDetails;
    }

    /**
     * @return The DTO containing the course description, sections, meeting times, and professor details/ratings.
     */
    public DisplayCourseDetails getCourseDetails() {
        return courseDetails;
    }
}