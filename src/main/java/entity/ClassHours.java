package entity;

import java.util.BitSet;

/**
 * Represents a weekly schedule in the form of a collection of halfhours (since classes can
 * sometimes end at halfhour intervals), where every halfhour is marked as either
 * occupied or unoccupied.
 */
public class ClassHours {
    private static final int DAYS_PER_WEEK = 7;
    private static final int HALF_HOURS_PER_DAY = 48;
    private static final int NUMBER_OF_BITS = DAYS_PER_WEEK * HALF_HOURS_PER_DAY;

    private BitSet halfHours = new BitSet(NUMBER_OF_BITS);

    public ClassHours() {
        // todo: come up with a friendly and inviting way to instantiate this class
    }

    private ClassHours(BitSet halfHours) {
        // This constructor allows for instantiating by providing the actual hours.
        // This is an implementation detail that should only be used by methods within this class.

        if (halfHours.length() != NUMBER_OF_BITS) {
            throw new IllegalArgumentException(
                    String.format("Must provide BitSet with %d bits.", NUMBER_OF_BITS));
        }

        this.halfHours = halfHours;
    }

    /**
     * @param hours A collection of ClassHours objects to union.
     * @return A new ClassHours object where every halfhour is occupied if ANY of the given
     * ClassHours had that halfhour marked as occupied.
     */
    public static ClassHours union(Iterable<ClassHours> hours) {
        BitSet newHalfHours = new BitSet(NUMBER_OF_BITS);
        hours.forEach(classHours -> newHalfHours.or(classHours.getHalfHours()));
        return new ClassHours(newHalfHours);
    }

    /**
     * @param other Another ClassHours object that you want to combine this one with.
     * @return A new ClassHours object that marks as occupied all halfhours where
     * either of the two given ClassHours were occupied.
     */
    public ClassHours union(ClassHours other) {
        BitSet newHalfHours = halfHours;
        newHalfHours.or(other.getHalfHours());
        return new ClassHours(newHalfHours);
    }

    public boolean doesIntersect(ClassHours other) {
        return halfHours.intersects(other.getHalfHours());
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

        int indexOfFirstTrueBlock = halfHours.nextSetBit(0);
        if (indexOfFirstTrueBlock == -1) {
            // No hours are marked as occupied.
            return true;
        }

        int indexOfNextFalseBit = halfHours.nextClearBit(indexOfFirstTrueBlock);
        if (indexOfNextFalseBit == -1) {
            // The rest of the hours are all marked as occupied.
            return true;
        }

        int indexOfSecondTrueBlock = halfHours.nextSetBit(indexOfNextFalseBit);

        // If there is no second block of TRUEs, then that means there is only
        // one block of trues, which makes us contiguous.
        return indexOfSecondTrueBlock == -1;
    }


    /**
     * @return the bitset used to represent the halfhours in a week
     */
    private BitSet getHalfHours() {
        // The bitset used to store the halfhours is an implementation detail that
        // shouldn't be seen by other classes, but is needed by some of the static
        // methods in this class.
        return halfHours;
    }
}
