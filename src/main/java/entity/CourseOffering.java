package entity;

import java.util.HashSet;
import java.util.Set;

public class CourseOffering {
    private final String uniqueIdentifier;
    private final CourseCode courseCode;
    private final String title;
    private final String description;
    private final Set<Section> availableSections = new HashSet<>();
    private boolean isUnknown = false;

    public CourseOffering(String uniqueIdentifier, CourseCode courseCode,
                          String title, String description) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public static CourseOffering createUnknownCourseOffering(String uniqueIdentifier) {
        CourseOffering offering = new CourseOffering(
                uniqueIdentifier,
                new CourseCode(uniqueIdentifier.substring(0, 8)),
                uniqueIdentifier,
                "Unknown Course");
        offering.isUnknown = true;
        return offering;
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

    public boolean isUnknown() {
        return isUnknown;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }
}
