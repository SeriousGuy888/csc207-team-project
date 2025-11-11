package use_case.constraints;


import entity.Section;
import java.util.Set;

/**
 * Ensures that specific "locked" sections must appear in the timetable.
 */
public class LockedSectionConstraint implements Constraint {
    private final Section lockedSection;

    public LockedSectionConstraint(Section lockedSection) {
        this.lockedSection = lockedSection;
    }

    @Override
    public boolean isSatisfiedBy(Set<Section> chosen) {
        // valid as long as chosen doesn't *explicitly exclude* the locked section
        // (i.e., once timetable is final, must contain it)
        return chosen.isEmpty() || chosen.contains(lockedSection);
    }

    //Only add this if needed later- public Section required() { return locked; }
}
