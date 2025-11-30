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

    // Application-level state for what is currently locked
    private final Set<Section> lockedSections = new HashSet<>();

    public LockSectionsInteractor(LockSectionsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(LockSectionsInputData inputData) {
        lockedSections.clear();
        lockedSections.addAll(inputData.getSectionsToLock());

        LockSectionsOutputData outputData =
                new LockSectionsOutputData(new HashSet<>(lockedSections));

        presenter.prepareSuccessView(outputData);
    }

    /**
     * Exposes the current locked sections for other use cases (e.g. Autogen) later.
     */
    public Set<Section> getLockedSections() {
        return new HashSet<>(lockedSections);
    }
}
