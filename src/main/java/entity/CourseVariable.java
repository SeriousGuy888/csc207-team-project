package entity;

import java.util.Set;

/** One course variable with its candidate sections (sections of a single CourseOffering). */
public class CourseVariable {
    private final CourseOffering courseOffering;
    private final Set<Section> domain;

    public CourseVariable(CourseOffering courseOffering, Set<Section> domain) {
        this.courseOffering = courseOffering;
        this.domain = domain;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public Set<Section> getDomain() {
        return domain;
    }
}
