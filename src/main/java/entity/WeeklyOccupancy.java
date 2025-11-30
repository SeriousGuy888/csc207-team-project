package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a weekly schedule with blocks of time
 * marked as either occupied or unoccupied.
 */
public class WeeklyOccupancy {
    private static final int MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    private static final int MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;

    // This class uses milliseconds because I noticed the official TTB's data specifies
    // start and end times in milliseconds, and this constructor would be convenient if we
    // use that data.

    /**
     * The timespans that contain all the time that
     * this {@link WeeklyOccupancy} considers marked occupied.
     *
     * <h2>Representation Invariants</h2>
     * <ol>
     * <li>Timespans do not overlap.</li>
     * <li>Timespans are ordered from earliest to latest.</li>
     * </ol>
     * <p>
     * With these invariants, I'm pretty sure there's exactly one unique
     * representation as this list for every possible set of milliseconds occupied.
     */
    private final List<MillisecondSpan> timespans;


    /**
     * Instantiate a WeeklyOccupancy with one time block that does not take place across multiple days.
     * Specify the start and end time in milliseconds and WeeklyOccupancy will be created, with
     * <ul>
     *     <li>time <em>including</em> start time marked occupied</li>
     *     <li>time up to but <em>excluding</em> end time marked occupied</li>
     * </ul>
     *
     * @param dayOfTheWeek          the day on which this time block occurs
     * @param startTimeMilliseconds start time, between 0 and 86,400,000
     * @param endTimeMilliseconds   end time, between 0 and 86,400,000. Should be later than start time.
     */
    public WeeklyOccupancy(DayOfTheWeek dayOfTheWeek,
                           int startTimeMilliseconds,
                           int endTimeMilliseconds) {
        if (startTimeMilliseconds < 0 || startTimeMilliseconds > MILLISECONDS_PER_DAY ||
                endTimeMilliseconds < 0 || endTimeMilliseconds > MILLISECONDS_PER_DAY) {
            throw new IllegalArgumentException("Start/end time specified was out of bounds of one day.");
        }
        if (startTimeMilliseconds >= endTimeMilliseconds) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        timespans = new ArrayList<>();
        timespans.add(new MillisecondSpan(
                dayOfTheWeek.millisecondOffset + startTimeMilliseconds,
                dayOfTheWeek.millisecondOffset + endTimeMilliseconds));
    }

    /**
     * @implNote Adherance of given {@code timespans} to representation invariants is not checked by this constructor
     * because the constructor is private.
     */
    private WeeklyOccupancy(List<MillisecondSpan> timespans) {
        this.timespans = new ArrayList<>(timespans);
    }

    /**
     * @return the day of the week that this {@link WeeklyOccupancy} occupies, or -1 if it does not occur on any day.
     */
    public int getDayOfTheWeek() {
        if (timespans.isEmpty()) {
            return -1;
        }
        return timespans.get(0).start / MILLISECONDS_PER_DAY;
    }

    /**
     * @return the start time of this {@link WeeklyOccupancy} in its specific day in milliseconds,
     * or -1 if it does not occur on any day.
     */
    public int getStartTimeInDay() {
        if (timespans.isEmpty()) {
            return -1;
        }
        return timespans.get(0).start % MILLISECONDS_PER_DAY;
    }

    /**
     * @param occupancies A collection of {@link WeeklyOccupancy} objects to union.
     * @return A new {@link WeeklyOccupancy} object where every halfhour is occupied if ANY of the given
     * {@link WeeklyOccupancy} had that halfhour marked as occupied.
     */
    public static WeeklyOccupancy union(Iterable<WeeklyOccupancy> occupancies) {
        // pretty sure this is \Theta(n \log n) in runtime

        List<MillisecondSpan> allSpans = new ArrayList<>();
        occupancies.forEach(weeklyOccupancy -> {
            allSpans.addAll(weeklyOccupancy.timespans);
        });

        if (allSpans.isEmpty()) {
            return new WeeklyOccupancy(allSpans);
        }

        // sort in ascending order based on start time
        // \Theta(n \log n), according to the .sort() implementation note, where n is the number of spans
        allSpans.sort(Comparator.comparingInt(a -> a.start));

        // merge consecutive spans if they overlap or are adjacent.
        // this relies on the spans being sorted in ascending order
        // \Theta(n) i think? since we're just making one pass over the spans, and each operation inside is constant
        List<MillisecondSpan> mergedSpans = new ArrayList<>();
        MillisecondSpan current = allSpans.get(0); // this is the span we're trying to coalesce later spans onto
        for (int i = 1; i < allSpans.size(); i++) {
            MillisecondSpan next = allSpans.get(i);
            if (current.overlapsOrNeighbours(next)) {
                // keep sticking the next span onto `current`, until we can't anymore
                current = MillisecondSpan.merge(current, next);
            } else {
                // if there's a gap between `current` and `next`, give up on `current` and start adding to `next`
                mergedSpans.add(current);
                current = next;
            }
        }
        mergedSpans.add(current); // when there's nothing left to add onto `current`

        return new WeeklyOccupancy(mergedSpans);
    }

    /**
     * @param other Another {@link WeeklyOccupancy} object that you want to combine this one with.
     * @return A <em>new</em> {@link WeeklyOccupancy} object that marks as occupied all halfhours where
     * either of the two given {@link WeeklyOccupancy} were occupied.
     */
    public WeeklyOccupancy union(WeeklyOccupancy other) {
        return WeeklyOccupancy.union(List.of(this, other));
    }

    /**
     * @param other {@link WeeklyOccupancy} that you want to compare to
     * @return true if there's any piece of time that is contained in both timespans, false otherwise.
     */
    public boolean doesIntersect(WeeklyOccupancy other) {
        // \Theta(m \times n) time complexity :(
        // but it's ok because our WeeklyOccupancy instances will probably never have more than like 50 blocks anyway

        for (MillisecondSpan thisPair : timespans) {
            for (MillisecondSpan thatPair : other.timespans) {
                if (thisPair.overlaps(thatPair)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if the nth half hour has <em>any</em> millisecond marked occupied.
     *
     * @param halfHourIndex A <strong>zero-indexed</strong> number from 0 to 47 representing which
     *                      thirty minute timespan to check.
     *                      For example,
     *                      <ul>
     *                        <li>00:00 to 00:30 is {@code 0},</li>
     *                        <li>00:30 to 01:00 is {@code 1},</li>
     *                        <li>12:00 to 12:30 is {@code 24}, and</li>
     *                        <li>23:30 to 00:00 is {@code 47}.</li>
     *                      </ul>
     * @return true if the specified half hour has at least one millisecond marked occupied, false otherwise.
     */
    public boolean checkOccupancyOfHalfHourSlot(DayOfTheWeek dayOfTheWeek, int halfHourIndex) {
        int start = dayOfTheWeek.millisecondOffset + (MILLISECONDS_PER_HOUR / 2) * halfHourIndex;
        int end = dayOfTheWeek.millisecondOffset + (MILLISECONDS_PER_HOUR / 2) * (halfHourIndex + 1);
        MillisecondSpan halfHour = new MillisecondSpan(start, end);

        return timespans.stream().anyMatch(timespan -> timespan.overlaps(halfHour));
    }

    /**
     * @return whether all occupied timeslots are contiguous.
     * <ul>
     * <li>"Contiguous" means every occupied timeslot is connected to every other occupied timeslot.</li>
     * <li>"Not contiguous" means there exists a timeslot disconnected to some other occupied timeslot.</li>
     * </ul>
     * @implNote if we have two timespans [23:00 Sunday, 00:00 Monday) and [00:00 Monday, 01:00 Monday),
     * this function will incorrectly return false, even though these timespans are actually contiguous
     * <strong>However,</strong> I have decided that I don't care.
     * There's no way classes can occur at these times anyway.
     */
    public boolean isContiguous() {
        return timespans.size() == 1 || timespans.isEmpty();
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WeeklyOccupancy)) {
            return false;
        }

        WeeklyOccupancy otherOccupancy = (WeeklyOccupancy) other;
        return this.timespans.equals(otherOccupancy.timespans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timespans);
    }


    public enum DayOfTheWeek {
        MONDAY(0),
        TUESDAY(MILLISECONDS_PER_DAY),
        WEDNESDAY(MILLISECONDS_PER_DAY * 2),
        THURSDAY(MILLISECONDS_PER_DAY * 3),
        FRIDAY(MILLISECONDS_PER_DAY * 4),
        SATURDAY(MILLISECONDS_PER_DAY * 5),
        SUNDAY(MILLISECONDS_PER_DAY * 6);

        final int millisecondOffset;

        DayOfTheWeek(int millisecondOffset) {
            this.millisecondOffset = millisecondOffset;
        }
    }

    /**
     * A span of time over a week, denoted using an inclusive start millisecond and exclusive end millisecond.
     */
    static final class MillisecondSpan {
        // class marked package-private (instead of private) so that i can access it from a test class

        // don't worry
        // Integer.MAX_VALUE is 2,147,483,647
        // and there are a measly 604,800,000 milliseconds in 1 week
        public final int start;
        public final int end;

        /**
         * <strong>Representation Invariant:</strong> start < end
         *
         * @implNote This constructor does not check the validity of the bounds
         * since this class is only intended to be used as part of {@link WeeklyOccupancy}'s implementation.
         */
        public MillisecondSpan(int start, int end) {
            this.start = start;
            this.end = end;
        }

        /**
         * @return true if there exists a time that is contained in both {@code this} and {@code other},
         * false otherwise.
         */
        public boolean overlaps(MillisecondSpan other) {
            return start < other.end && other.start < end;
            /*
                Proof: Let [a, b) and [x, y) be nonempty intervals. So (a < b) and (x < y).
                (==>) Assume that [a, b) and [x, y) overlap (i.e. some millisecond is in both intervals)
                    Then \exists t, such that (a <= t < b) and (x <= t < y).
                    Then (a <= t < y) so (a < y), and (x <= t < b) so (x < b), as needed.
                (<==) Assume that a < y and x < b.
                    Choose t = max(a, x).
                    If (t = a), then t \in [a, b) trivially. And (x <= a < y), so t \in [x, y).
                    If (t = x), then t \in [x, y) trivially. And (a <= x < b), so t \in [a, b).
            */
        }

        /**
         * @return whether {@code this} and {@code other} contain the same millisecond or consecutive milliseconds
         * For example, [1, 3) and [2, 4) overlap; [1, 3) and [3, 5) are neighbours,
         * so this method would return true on both pairs.
         */
        public boolean overlapsOrNeighbours(MillisecondSpan other) {
            return overlaps(other) || this.start == other.end || this.end == other.start;
        }

        /**
         * @return a <em>new</em> {@link MillisecondSpan}
         * whose start is the earliest of the starts of {@code a} and {@code b}, and
         * whose end is the latest of the ends of {@code a} and {@code b}.
         * <p>
         * When {@code a} and {@code b} overlap or are neighbours, this has the effect of the new span representing
         * exactly the union of the times covered by {@code a} and {@code b}.
         */
        public static MillisecondSpan merge(MillisecondSpan a, MillisecondSpan b) {
            return new MillisecondSpan(Math.min(a.start, b.start), Math.max(a.end, b.end));
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof MillisecondSpan)) {
                return false;
            }

            MillisecondSpan otherSpan = (MillisecondSpan) other;
            return otherSpan.start == this.start && otherSpan.end == this.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }
}
