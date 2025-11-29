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
        // Replace the existing locked set with the new one from input.
        lockedSections.clear();
        lockedSections.addAll(inputData.getSectionsToLock());

        LockSectionsOutputData outputData =
                new LockSectionsOutputData(lockedSections);
        presenter.present(outputData);
    }

    /**
     * Exposes the current locked sections.
     * Useful later when other use cases (e.g. autogen) need this info.
     */
    public Set<Section> getLockedSections() {
        return new HashSet<>(lockedSections);
    }
}
