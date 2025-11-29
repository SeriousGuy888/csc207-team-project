package interface_adapter.locksections;

import entity.Section;
import use_case.locksections.LockSectionsInputBoundary;
import use_case.locksections.LockSectionsInputData;

import java.util.Set;

public class LockSectionsController {

    private final LockSectionsInputBoundary interactor;

    public LockSectionsController(LockSectionsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void updateLockedSections(Set<Section> sectionsToLock) {
        LockSectionsInputData inputData = new LockSectionsInputData(sectionsToLock);
        interactor.execute(inputData);
    }
}
