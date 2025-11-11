package auto;

//import entity.ClassHours;
import java.util.Set;

public class Constraints {
    private  ClassHours blocked;          // all half-hours user marks as unavailable
    private  Set<LockedKey> lockedSections;  // preselected sections

    public Constraints(ClassHours blocked, Set<LockedKey> lockedSections) {
        this.blocked = blocked == null ? new ClassHours() : blocked;
        this.lockedSections = lockedSections == null ? Set.of() : lockedSections;
    }

    public ClassHours getBlocked() { return blocked; }
    public Set<LockedKey> getLockedSections() { return lockedSections; }
}
