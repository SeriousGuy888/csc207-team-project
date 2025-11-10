package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @return `true` if the section added was not already in this timetable.
     */
    public boolean addSection(Section section) {
        return sections.add(section);
    }

    /**
     * @param section The section to remove from this timetable
     * @return `true` if the timetable did actually contain the section removed
     */
    public boolean removeSection(Section section) {
        return sections.remove(section);
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
}
