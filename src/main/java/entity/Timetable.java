package entity;

import java.util.HashSet;
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
        // todo: implement timetable conflict checking
        throw new UnsupportedOperationException();
    }
}
