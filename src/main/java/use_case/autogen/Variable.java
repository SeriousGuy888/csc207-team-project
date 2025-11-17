package use_case.autogen;

import entity.CourseOffering;
import entity.Section;
import java.util.Set;

/** One course variable with its candidate sections (sections of a single CourseOffering). */
public class Variable {
    private final CourseOffering courseOffering;
    private final Set<Section> domain;

    public Variable(CourseOffering courseOffering, Set<Section> domain) {
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
