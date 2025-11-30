package interface_adapter.locksections;

import entity.Section;
import use_case.locksections.LockSectionsInputBoundary;
import use_case.locksections.LockSectionsInputData;

import java.util.Set;

/**
 * Controller for the Lock Sections use case.
 * Called by the View (e.g., TimetablePanel) when the user checks/unchecks sections.
 */
public class LockSectionsController {

    private final LockSectionsInputBoundary interactor;

    public LockSectionsController(LockSectionsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Updates the set of locked sections by passing them into the use case.
     *
     * @param sectionsToLock the sections that should be locked
     */
    public void updateLockedSections(Set<Section> sectionsToLock) {
        LockSectionsInputData inputData = new LockSectionsInputData(sectionsToLock);
        interactor.execute(inputData);
    }
}
