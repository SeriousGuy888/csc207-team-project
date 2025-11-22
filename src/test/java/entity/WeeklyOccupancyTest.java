package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeeklyOccupancyTest {
    private static final int MILLISECONDS_PER_HOUR = 1000 * 60 * 60;

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
}
