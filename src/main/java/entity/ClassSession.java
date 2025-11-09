package entity;

/**
 * Represents a session of a section.
 * <p>
 * This is needed because
 * each section of a class might have class multiples a week,
 * such as once on Monday, once on Tuesday, once on Thursday, and each
 * session might be in a different location/time from the others.
 */
public class ClassSession {
    private UofTLocation location;
    private ClassHours time; // must be contiguous timespan

    public UofTLocation getLocation() {
        return location;
    }

    public ClassHours getTime() {
        return time;
    }
}
