package use_case.display_course_context;

public interface DisplayCourseDetailsDataAccessInterface {
    DisplayCourseDetails getCourseDetails(String courseId);

    /**
     * Get the professor's name for a course and section.
     * @param courseId The full course offer identifier (e.g., "CSC108F").
     * @param sectionId The section ID (e.g., "LEC-0101").
     * @return The professor's name, or "TBD Professor" if not found.
     */
    public String getProfessorNameByCourseAndSection(String courseId, String sectionId);
}

