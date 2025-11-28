package use_case.display_course_context;

import java.util.List;

public class DisplayCourseDetails {
    private final String courseTitle;
    private final String courseDescription;
    private final List<DisplaySectionDetails> sections;

    public DisplayCourseDetails(String courseTitle, String courseDescription, List<DisplaySectionDetails> sections) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.sections = sections;
    }

    // Getters
    public String getCourseTitle() { return courseTitle; }
    public String getCourseDescription() { return courseDescription; }
    public List<DisplaySectionDetails> getSections() { return sections; }
}
