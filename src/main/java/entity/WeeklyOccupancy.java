package entity;

import java.time.DayOfWeek;
import java.util.BitSet;

/**
 * Represents a weekly schedule in the form of a collection of half-hour timeslots
 * (since classes can sometimes end at halfhour intervals),
 * where every half-hour is marked as either occupied or unoccupied.
 */
public class WeeklyOccupancy {
    /*
        I don't _think_ classes can every occur on weekends or outside the 09:00-21:00 range,
        so since we're representing all the timeslots on all seven days, some of these bits
        might just always be zero, but I think it would be more confusing if parts of the week
        were just not representable by this class.

        It's not a big deal that some bits are always zero.
    */
    private static final int DAYS_PER_WEEK = 7;
    private static final int HALF_HOURS_PER_DAY = 48;
    private static final int NUMBER_OF_TIMESLOTS = DAYS_PER_WEEK * HALF_HOURS_PER_DAY;

    private BitSet halfHourSlots = new BitSet(NUMBER_OF_TIMESLOTS);


    /**
     * Instantiate a WeeklyOccupancy with one time block that does not take place across multiple days.
     * Specify the start and end time in milliseconds and WeeklyOccupancy will be created, with
     * <ul>
     *     <li>timeslot containing start time marked occupied</li>
     *     <li>timeslots up to but excluding end time marked occupied</li>
     * </ul>
     *
     * @param dayOfTheWeek          the day on which this time block occurs
     * @param startTimeMilliseconds start time, between 0 and 86,400,000
     * @param endTimeMilliseconds   end time, between 0 and 86,400,000. Should be later than start time.
     */
    public WeeklyOccupancy(DayOfTheWeek dayOfTheWeek,
                           int startTimeMilliseconds,
                           int endTimeMilliseconds) {
        // todo: test this constructor to make sure it actually works
        // i haven't written any tests yet because I don't know yet if we are actually going to use this constructor
        // or if we're gonna need a separate constructor anyway, depending on what format our data is in

        // This constructor uses milliseconds because I noticed the official TTB's data specifies
        // start and end times in milliseconds, and this constructor would be convenient if we
        // use that data.
        final int MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
        final int MILLISECONDS_PER_TIMESLOT = MILLISECONDS_PER_DAY / HALF_HOURS_PER_DAY;

        if (startTimeMilliseconds < 0 || startTimeMilliseconds > MILLISECONDS_PER_DAY ||
                endTimeMilliseconds < 0 || endTimeMilliseconds > MILLISECONDS_PER_DAY) {
            throw new IllegalArgumentException("Start/end time specified was out of bounds of one day.");
        }
        if (endTimeMilliseconds - startTimeMilliseconds < MILLISECONDS_PER_TIMESLOT) {
            throw new IllegalArgumentException(String.format(
                    "End time must be greater than start time by at least %d milliseconds.",
                    MILLISECONDS_PER_TIMESLOT));
        }

        int dayBitOffset = dayOfTheWeek.bitOffset;
        int startTimeBitOffset = startTimeMilliseconds / MILLISECONDS_PER_TIMESLOT;
        int endTimeBitOffset = endTimeMilliseconds / MILLISECONDS_PER_TIMESLOT;

        halfHourSlots.set(dayBitOffset + startTimeBitOffset, dayBitOffset + endTimeBitOffset);
    }

    private WeeklyOccupancy(BitSet halfHourSlots) {
        // This constructor allows for instantiating by providing the actual hours.
        // This is an implementation detail that should only be used by methods within this class.

        if (halfHourSlots.size() != NUMBER_OF_TIMESLOTS) {
            throw new IllegalArgumentException(
                    String.format("Must provide BitSet with %d bits.", NUMBER_OF_TIMESLOTS));
        }

        this.halfHourSlots = halfHourSlots;
    }

    /**
     * @param hours A collection of ClassHours objects to union.
     * @return A new ClassHours object where every halfhour is occupied if ANY of the given
     * ClassHours had that halfhour marked as occupied.
     */
    public static WeeklyOccupancy union(Iterable<WeeklyOccupancy> hours) {
        BitSet newHalfHours = new BitSet(NUMBER_OF_TIMESLOTS);
        hours.forEach(weeklyOccupancy -> newHalfHours.or(weeklyOccupancy.getHalfHourSlots()));
        return new WeeklyOccupancy(newHalfHours);
    }

    /**
     * @param other Another ClassHours object that you want to combine this one with.
     * @return A new ClassHours object that marks as occupied all halfhours where
     * either of the two given ClassHours were occupied.
     */
    public WeeklyOccupancy union(WeeklyOccupancy other) {
        BitSet newHalfHours = (BitSet) halfHourSlots.clone();
        newHalfHours.or(other.getHalfHourSlots());
        return new WeeklyOccupancy(newHalfHours);
    }

    public boolean doesIntersect(WeeklyOccupancy other) {
        return halfHourSlots.intersects(other.getHalfHourSlots());
    }

    /**
     * @return whether all occupied timeslots are contiguous.
     * <ul>
     * <li>"Contiguous" means every occupied timeslot is connected to every other occupied timeslot.</li>
     * <li>"Not contiguous" means there exists a timeslot disconnected to some other occupied timeslot.</li>
     * </ul>
     */
    public boolean isContiguous() {
        // This method can be used for verifying the validity of a ClassSession's time.

        int indexOfFirstTrueBlock = halfHourSlots.nextSetBit(0);
        if (indexOfFirstTrueBlock == -1) {
            // No hours are marked as occupied.
            return true;
        }

        int indexOfNextFalseBit = halfHourSlots.nextClearBit(indexOfFirstTrueBlock);
        if (indexOfNextFalseBit == -1) {
            // The rest of the hours are all marked as occupied.
            return true;
        }

        int indexOfSecondTrueBlock = halfHourSlots.nextSetBit(indexOfNextFalseBit);

        // If there is no second block of TRUEs, then that means there is only
        // one block of trues, which makes us contiguous.
        return indexOfSecondTrueBlock == -1;
    }


    /**
     * @return the bitset used to represent the halfhours in a week
     */
    private BitSet getHalfHourSlots() {
        // The bitset used to store the halfhours is an implementation detail that
        // shouldn't be seen by other classes, but is needed by some of the static
        // methods in this class.
        return halfHourSlots;
    }

    public enum DayOfTheWeek {
        MONDAY(0),
        TUESDAY(HALF_HOURS_PER_DAY),
        WEDNESDAY(HALF_HOURS_PER_DAY * 2),
        THURSDAY(HALF_HOURS_PER_DAY * 3),
        FRIDAY(HALF_HOURS_PER_DAY * 4),
        SATURDAY(HALF_HOURS_PER_DAY * 5),
        SUNDAY(HALF_HOURS_PER_DAY * 6);

        final int bitOffset;

        DayOfTheWeek(int bitOffset) {
            this.bitOffset = bitOffset;
        }
    }
}
