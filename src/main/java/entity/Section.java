package entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Section {
    private final CourseOffering courseOffering;
    private final String sectionName;
    private final TeachingMethod teachingMethod;
    private final Set<Meeting> meetings = new HashSet<>();

    /**
     * @param courseOffering the CourseOffering that this section belongs to
     * @param sectionName (e.g. "LEC0101", "TUT5202")
     * @param teachingMethod (e.g. Lecture, Tutorial)
     */
    public Section(CourseOffering courseOffering, String sectionName, TeachingMethod teachingMethod) {
        this.courseOffering = courseOffering;
        this.sectionName = sectionName;
        this.teachingMethod = teachingMethod;
    }

    /**
     * @return Union of all time slots that are occupied by the sessions in this section,
     * in other words, the times at which this section has class.
     */
    public WeeklyOccupancy getOccupiedTime() {
        List<WeeklyOccupancy> hoursList = meetings
                .stream()
                .map(Meeting::getTime)
                .collect(Collectors.toList());
        return WeeklyOccupancy.union(hoursList);
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public String getSectionName() {
        return sectionName;
    }

    public TeachingMethod getTeachingMethod() {
        return teachingMethod;
    }

    /**
     * @param meeting the meeting to add
     * @return `true` if the meeting wasn't already one of the meetings of this section
     */
    public boolean addMeeting(Meeting meeting) {
        return meetings.add(meeting);
    }

    /**
     * @param meeting the meeting to add
     * @return `true` if the meeting removed was actually a meeting in this section.
     */
    public boolean removeMeeting(Meeting meeting) {
        return meetings.remove(meeting);
    }

    /**
     * @return a <strong>copy</strong> of the set of meetings associated with this section.
     */
    public Set<Meeting> getMeetings() {
        return new HashSet<>(meetings);
    }

    public enum TeachingMethod {
        LECTURE,
        TUTORIAL,
        PRACTICAL,
    }
}
