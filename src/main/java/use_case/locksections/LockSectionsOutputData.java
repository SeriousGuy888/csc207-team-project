package use_case.locksections;

import entity.Section;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Output data for the Lock Sections use case.
 * Represents the resulting set of locked sections after the update.
 */
public class LockSectionsOutputData {

    private final Set<Section> lockedSections;

    public LockSectionsOutputData(Set<Section> lockedSections) {
        this.lockedSections = lockedSections == null
                ? new HashSet<>()
                : new HashSet<>(lockedSections);
    }

    /**
     * @return an unmodifiable view of the locked sections.
     */
    public Set<Section> getLockedSections() {
        return Collections.unmodifiableSet(lockedSections);
    }
}
