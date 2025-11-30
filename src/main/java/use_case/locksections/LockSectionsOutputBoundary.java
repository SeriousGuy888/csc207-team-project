package use_case.locksections;

/**
 * Output boundary for the Lock Sections use case.
 * Implemented by the presenter.
 */
public interface LockSectionsOutputBoundary {

    /**
     * Called when locking sections succeeds.
     *
     * @param outputData the resulting locked sections
     */
    void prepareSuccessView(LockSectionsOutputData outputData);
}
