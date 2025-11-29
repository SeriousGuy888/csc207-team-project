package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeeklyOccupancyTest {
    private static final int MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    private static final int MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;

    WeeklyOccupancy NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            9 * MILLISECONDS_PER_HOUR,
            10 * MILLISECONDS_PER_HOUR);
    WeeklyOccupancy TEN_OCLOCK_TO_ELEVEN_OCLOCK_MONDAY = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            10 * MILLISECONDS_PER_HOUR,
            11 * MILLISECONDS_PER_HOUR);
    WeeklyOccupancy ELEVEN_OCLOCK_TO_TWELVE_OCLOCK_MONDAY = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            11 * MILLISECONDS_PER_HOUR,
            12 * MILLISECONDS_PER_HOUR);

    private static final int HALF_HOUR_INDEX_OF_NINE_OCLOCK_TO_NINE_THIRTY = 9 * 2;
    private static final int HALF_HOUR_INDEX_OF_NINE_THIRTY_TO_TEN_OCLOCK = 9 * 2 + 1;
    private static final int HALF_HOUR_INDEX_OF_TEN_OCLOCK_TO_TEN_THIRTY = 10 * 2;

    @Test
    void constructorRejectsOutOfBoundsStartAndEnd() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.MONDAY, -1, 0)
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.MONDAY, 0, MILLISECONDS_PER_DAY + 1)
        );
    }

    @Test
    void constructorRejectsEmptyAndNegativeIntervals() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.MONDAY, 1000, 1000)
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.MONDAY, 1000, 500)
        );
    }

    @Test
    void consecutiveHoursHasContiguousUnion() {
        WeeklyOccupancy union = NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.union(TEN_OCLOCK_TO_ELEVEN_OCLOCK_MONDAY);
        assertTrue(union.isContiguous());
    }

    @Test
    void nonConsecutiveHoursHasNoncontiguousUnionButYouCanAddTheMissingTimeBetweenThemToMakeThemContiguous() {
        WeeklyOccupancy union1 = NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.union(ELEVEN_OCLOCK_TO_TWELVE_OCLOCK_MONDAY);
        assertFalse(union1.isContiguous());

        WeeklyOccupancy union2 = union1.union(TEN_OCLOCK_TO_ELEVEN_OCLOCK_MONDAY);
        assertTrue(union2.isContiguous());
    }

    @Test
    void consecutiveHoursJustBarelyDontIntersect() {
        assertFalse(NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.doesIntersect(TEN_OCLOCK_TO_ELEVEN_OCLOCK_MONDAY));
    }

    @Test
    void correctlyChecksOccupancyOfHalfHourSlots() {
        assertTrue(
                NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.checkOccupancyOfHalfHourSlot(
                        WeeklyOccupancy.DayOfTheWeek.MONDAY,
                        HALF_HOUR_INDEX_OF_NINE_OCLOCK_TO_NINE_THIRTY)
        );
        assertTrue(
                NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.checkOccupancyOfHalfHourSlot(
                        WeeklyOccupancy.DayOfTheWeek.MONDAY,
                        HALF_HOUR_INDEX_OF_NINE_THIRTY_TO_TEN_OCLOCK)
        );
        assertFalse(
                NINE_OCLOCK_TO_TEN_OCLOCK_MONDAY.checkOccupancyOfHalfHourSlot(
                        WeeklyOccupancy.DayOfTheWeek.MONDAY,
                        HALF_HOUR_INDEX_OF_TEN_OCLOCK_TO_TEN_THIRTY)
        );
    }
}
