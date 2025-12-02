// interface_adapter/lock_section/LockSectionController.java

package interface_adapter.locksections;

import use_case.locksections.LockSectionInputBoundary;
import use_case.locksections.LockSectionInputData;

public class LockSectionController {

    private final LockSectionInputBoundary interactor;

    public LockSectionController(LockSectionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void toggleLock(int tabIndex,
                           String courseCode,
                           String sectionName,
                           boolean locked) {
        LockSectionInputData data = new LockSectionInputData(
                tabIndex,
                courseCode,
                sectionName,
                locked
        );
        interactor.execute(data);
    }
}

