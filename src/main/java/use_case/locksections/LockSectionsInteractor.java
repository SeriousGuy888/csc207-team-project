package use_case.locksections;

import entity.Section;

import java.util.HashSet;
import java.util.Set;

/**
 * Interactor for the Lock Sections use case.
 * Maintains the current set of locked sections and updates it
 * based on input, then notifies the presenter.
 */
public class LockSectionsInteractor implements LockSectionsInputBoundary {

    private final LockSectionsOutputBoundary presenter;

    private final Set<Section> lockedSections = new HashSet<>();

    public LockSectionsInteractor(LockSectionsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(LockSectionsInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView("Input data for locking sections cannot be null.");
            return;
        }

        Set<Section> sectionsToLock = inputData.getSectionsToLock();
        if (sectionsToLock == null) {
            presenter.prepareFailView("Sections to lock cannot be null.");
            return;
        }

        lockedSections.clear();
        lockedSections.addAll(sectionsToLock);

        LockSectionsOutputData outputData =
                new LockSectionsOutputData(new HashSet<>(lockedSections));

        presenter.prepareSuccessView(outputData);
    }

    public Set<Section> getLockedSections() {
        return new HashSet<>(lockedSections);
    }
}
