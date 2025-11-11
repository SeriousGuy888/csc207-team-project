package use_case.autogen;


import entity.Section;
import java.util.Set;

/** One course variable with its candidate sections. */
public class Variable {
    private final String courseName;
    private final Set<Section> domain;

    public Variable(String courseName, Set<Section> domain) {
        this.courseName = courseName;
        this.domain = domain;
    }

    public String getCourseName() { return courseName; }
    public Set<Section> getDomain() { return domain; }
}
