package entity;

import java.util.Set;

public class Section {
    private CourseOffering courseOffering;
    private String sectionCode;
    private SectionType type;
    private Set<ClassSession> classSessions;

    /**
     * @return Union of all time slots that are occupied by the sessions in this section,
     *         in other words, the times at which this section has class.
     */
    public ClassHours getOccupiedTime() {
        // todo: implement getOccupiedTime
        throw new UnsupportedOperationException();
    }
}

enum SectionType {
    LECTURE,
    TUTORIAL
}