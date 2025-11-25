package entity;

import java.util.HashSet;
import java.util.Set;

public class CourseOffering {
    private final CourseCode courseCode;
    private final String title;
    private final String description;
    private final Set<Section> availableSections = new HashSet<>();

    public CourseOffering(CourseCode courseCode, String title, String description) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
    }

    /**
     * @param section the section to add
     * @return `true` if the section wasn't already one of the section of this course offering
     */
    public boolean addAvailableSection(Section section) {
        return availableSections.add(section);
    }

    /**
     * @param section the section to add
     * @return `true` if the section removed was actually a section in this course offering
     */
    public boolean removeAvailableSection(Section section) {
        return availableSections.remove(section);
    }

    /**
     * @return a <strong>copy</strong> of the set of available for this course.
     */
    public Set<Section> getAvailableSections() {
        return new HashSet<>(availableSections);
    }

    public CourseCode getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
