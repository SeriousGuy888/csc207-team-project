package entity;

import java.util.Set;

/**
 * Represents timetable created by a user, consisting of the course sections they selected.
 */
public class Timetable {
    private Set<Section> selectedSections;

    public void addSection(Section section) {
        selectedSections.add(section);
    }

    public void removeSection(Section section) {
        selectedSections.remove(section);
    }

    public Set<Section> getConflicts() {
        // todo: implement timetable conflict checking
        throw new UnsupportedOperationException();
    }

    public String serialise() {
        // todo: implement timetable serialisation
        throw new UnsupportedOperationException();
    }

    public static Timetable deserialise(String serialisedTimetable) {
        // todo: implement timetable deserialisation
        throw new UnsupportedOperationException();
    }
}
