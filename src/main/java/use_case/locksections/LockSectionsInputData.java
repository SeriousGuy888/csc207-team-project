package use_case.locksections;

import entity.Section;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Input data for the Lock Sections use case.
 * Represents the sections that should be locked after this call.
 */
public class LockSectionsInputData {

    private final Set<Section> sectionsToLock;

    public LockSectionsInputData(Set<Section> sectionsToLock) {
        this.sectionsToLock = sectionsToLock == null
                ? new HashSet<>()
                : new HashSet<>(sectionsToLock);
    }

    /**
     * @return a copy of the sections that should be locked.
     */
    public Set<Section> getSectionsToLock() {
        return Collections.unmodifiableSet(sectionsToLock);
    }
}
