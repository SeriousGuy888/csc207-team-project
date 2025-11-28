package entity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents timetable created by a user, consisting of the course sections they selected.
 */
public class Timetable {
    private final Set<Section> sections;

    public Timetable() {
        this.sections = new HashSet<>();
    }

    /**
     * @param section The section to add to this timetable
     * @return `true` if the section added was not already in this timetable and does not contain >2-course conflicts.
     */
    public boolean addSection(Section section) {
        boolean result = sections.add(section);
        boolean valid = markConflicts();
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
        boolean result = sections.remove(section);
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

    public Set<Section> getConflicts() {
        Set<Section> conflicts = new HashSet<>();

        // I think this implementation is \Theta(n^2) :pensive:
        // Probably not actually a problem though if we're not checking conflicts twenty times a second

        // I think I know a more efficient implementation, but I don't want to implement it right now:
        // - Create a list of "histogram bins", each bin being a set of sections.
        // - Loop through the sections and add each section to each of the bins of the slots it occupies (n steps)
        // - Loop through the time slots with more than 1 section and record them as conflicts (constant steps)

        // For each pair of distinct sections,
        // if their occupied times intersect, mark both as conflicting.
        List<Section> sectionsList = new ArrayList<>(sections);
        for (int i = 0; i < sectionsList.size(); i++) {
            Section a = sectionsList.get(i);
            for (int j = i + 1; j < sectionsList.size(); j++) {
                Section b = sectionsList.get(j);
                if (a.getOccupiedTime().doesIntersect(b.getOccupiedTime())) {
                    conflicts.add(a);
                    conflicts.add(b);
                }
            }
        }


        return conflicts;
    }

    public boolean markConflicts() {
        List<Meeting> allMeetings = sections.stream()
                .flatMap(section -> section.getMeetings().stream())
                .collect(Collectors.toList());

        for (Meeting meeting : allMeetings) {
            meeting.resetNumConflicts();
        }

        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting a = allMeetings.get(i);

            for (int j = i + 1; j < allMeetings.size(); j++) {
                Meeting b = allMeetings.get(j);

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
}
