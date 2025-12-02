package entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents timetable created by a user, consisting of the course sections they selected.
 */
public class Timetable {
    private final Set<Section> sections;
    private String timetableName = "Default Timetable";
    private final Set<Section> lockedSections = new HashSet<>();

    public Timetable() {
        this.sections = new HashSet<>();
    }
    public Timetable(Set<Section> sections) {this.sections = sections;}

    public String getTimetableName() {
        return timetableName;
    }

    public void setTimetableName(String timetableName) {
        this.timetableName = timetableName;
    }

    /**
     * @param section The section to add to this timetable
     * @return `true` if the section added was not already in this timetable and does not contain >2-course conflicts.
     */
    public boolean addSection(Section section) {
        final boolean result = sections.add(section);
        final boolean valid = markConflicts();
        if (result && valid) {
            return true;
        }
        else if (result) {
            this.removeSection(section);
            return false;
        }
        else {
            return false;
        }
    }

    /**
     * @param section The section to remove from this timetable
     * @return `true` if the timetable did actually contain the section removed
     */
    public boolean removeSection(Section section) {
        final boolean result = sections.remove(section);
        if (result) {
            markConflicts();
        }
        return result;
    }

    /**
     * @return a <strong>copy</strong> of the set of sections included in this timetable.
     */
    public Set<Section> getSections() {
        return new HashSet<>(sections);
    }

    /**
     * Removes all sections from this timetable.
     */
    public void clear() {
        sections.clear();
    }
    /**
     * Update the number of conflicts for each meeting in the timetable.
     * @return `true` if timetable does not contain any >2-course conflicts.
     */
    public boolean markConflicts() {
        final List<Meeting> allMeetings = sections.stream()
                .flatMap(section -> section.getMeetings().stream())
                .collect(Collectors.toList());

        for (Meeting meeting : allMeetings) {
            meeting.resetNumConflicts();
        }

        for (int i = 0; i < allMeetings.size(); i++) {
            final Meeting a = allMeetings.get(i);

            for (int j = i + 1; j < allMeetings.size(); j++) {
                final Meeting b = allMeetings.get(j);

                if (a.getSemester() == b.getSemester() && a.getTime().doesIntersect(b.getTime())) {
                    a.incrementNumConflicts();
                    b.incrementNumConflicts();
                }
            }
        }

        for (Meeting meeting : allMeetings) {
            if (meeting.getNumConflicts() > 1) {
                return false;
            }
        }
        return true;
    }

    public void lockSection(Section section) {
        lockedSections.add(section);
    }

    public void unlockSection(Section section) {
        lockedSections.remove(section);
    }

    public boolean isLocked(Section section) {
        return lockedSections.contains(section);
    }

    public Set<Section> getLockedSections() {
        return new HashSet<>(lockedSections);
    }
}
