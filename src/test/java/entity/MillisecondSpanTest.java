package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MillisecondSpanTest {
    @Test
    void overlapsSubsets() {
        WeeklyOccupancy.MillisecondSpan a = new WeeklyOccupancy.MillisecondSpan(0, 1000);
        WeeklyOccupancy.MillisecondSpan b = new WeeklyOccupancy.MillisecondSpan(250, 750);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    void overlapsEdges() {
        WeeklyOccupancy.MillisecondSpan a = new WeeklyOccupancy.MillisecondSpan(0, 500);
        WeeklyOccupancy.MillisecondSpan b = new WeeklyOccupancy.MillisecondSpan(499, 1000);
        WeeklyOccupancy.MillisecondSpan c = new WeeklyOccupancy.MillisecondSpan(500, 1000);
        WeeklyOccupancy.MillisecondSpan d = new WeeklyOccupancy.MillisecondSpan(501, 1000);
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
        assertFalse(a.overlaps(c));
        assertFalse(c.overlaps(a));
        assertFalse(a.overlaps(d));
        assertFalse(d.overlaps(a));
    }

    @Test
    void overlapsOrNeighboursEdges() {
        WeeklyOccupancy.MillisecondSpan a = new WeeklyOccupancy.MillisecondSpan(0, 500);
        WeeklyOccupancy.MillisecondSpan b = new WeeklyOccupancy.MillisecondSpan(499, 1000);
        WeeklyOccupancy.MillisecondSpan c = new WeeklyOccupancy.MillisecondSpan(500, 1000);
        WeeklyOccupancy.MillisecondSpan d = new WeeklyOccupancy.MillisecondSpan(501, 1000);

        assertTrue(a.overlapsOrNeighbours(b));
        assertTrue(b.overlapsOrNeighbours(a));
        assertTrue(a.overlapsOrNeighbours(c));
        assertTrue(c.overlapsOrNeighbours(a));
        assertFalse(a.overlapsOrNeighbours(d));
        assertFalse(d.overlapsOrNeighbours(a));
    }
}
