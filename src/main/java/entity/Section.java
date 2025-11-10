package entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Section {
    private CourseOffering courseOffering;
    private String sectionCode;
    private TeachingMethod teachingMethod;
    private final Set<Meeting> meetings = new HashSet<>();

    /**
     * @return Union of all time slots that are occupied by the sessions in this section,
     * in other words, the times at which this section has class.
     */
    public ClassHours getOccupiedTime() {
        List<ClassHours> hoursList = meetings
                .stream()
                .map(Meeting::getTime)
                .collect(Collectors.toList());
        return ClassHours.union(hoursList);
    }

    public enum TeachingMethod {
        LECTURE,
        TUTORIAL,
        PRACTICAL,
    }
}
