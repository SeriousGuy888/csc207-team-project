package entity;

/**
 * Represents a meeting of a course section.
 * <p>
 * This is needed because
 * each section of a class might have class multiples a week,
 * such as once on Monday, once on Tuesday, once on Thursday, and each
 * session might be in a different location/time from the others.
 */
public class Meeting {
    private final UofTLocation location;
    private final WeeklyOccupancy time; // must be contiguous timespan

    public Meeting(UofTLocation location, WeeklyOccupancy time) {
        this.location = location;
        this.time = time;
    }

    public UofTLocation getLocation() {
        return location;
    }

    public WeeklyOccupancy getTime() {
        return time;
    }
}
