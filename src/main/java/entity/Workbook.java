package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a project file created by a user.
 * Can hold potentially multiple timetables, all saved as part of one file.
 */
public class Workbook implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Set<Timetable> timetables;
//    private final List<Timetable> timetables;

    public Workbook(List<Timetable> timetables) {
        this.timetables = new HashSet<>(timetables);
//        this.timetables = timetables;
    }

    /**
     * @param timetable The timetable to add to this workbook.
     * @throws IllegalArgumentException if the timetable provided is already in this workbook.
     */
    public void addTimetable(Timetable timetable) {
        if (timetables.contains(timetable)) {
            throw new IllegalArgumentException("The provided timetable is already contained in this workbook.");
        }
        timetables.add(timetable);
    }

    /**
     * @param timetable The timetable to remove from this workbook.
     * @return `true` if the workbook actually contained the specified timetable.
     */
    public boolean removeTimetable(Timetable timetable) {
        return timetables.remove(timetable);
    }

    /**
     * @return a <strong>copy</strong> of the list of the timetables contained in this workbook.
     */
    public List<Timetable> getTimetables() {
        // Return a copy to prevent clients from modifying the list
        return new ArrayList<>(timetables);
    }
}
