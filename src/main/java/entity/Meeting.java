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
    private final Semester semester;
    private final WeeklyOccupancy time;
    private int numConflicts;

    public Meeting(UofTLocation location, Semester semester, WeeklyOccupancy time) {
        this.location = location;
        this.semester = semester;
        this.time = time;
    }

    public UofTLocation getLocation() {
        return location;
    }

    public Semester getSemester() {
        return semester;
    }

    public WeeklyOccupancy getTime() {
        return time;
    }

    public int getDayOfTheWeek() {
        return time.getDayOfTheWeek();
    }

    public int getStartTimeIndexInDay() {
        return time.getStartIndexInDay();
    }

    public int getNumConflicts() {
        return numConflicts;
    }

    /**
     * Increments the number of conflict this meeting.
     */
    public void incrementNumConflicts() {
        this.numConflicts++;
    }

    /**
     * Resets the number of conflicts to 0.
     */
    public void resetNumConflicts() {
        this.numConflicts = 0;
    }

    public boolean checkOccupancy(int day, int timeSlot) {
        final int bitIndex = day * 48 + timeSlot;
        return time.getHalfHourSlots().get(bitIndex);
    }

    public enum Semester {
        FIRST,
        SECOND
    }
}
