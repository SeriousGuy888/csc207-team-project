package use_case.locksections;

/**
 * Output boundary for the Lock Sections use case.
 * Implemented by the presenter.
 */
public interface LockSectionsOutputBoundary {

    /**
     * Presents the result of updating the locked sections.
     *
     * @param outputData the resulting locked sections.
     */
    void present(LockSectionsOutputData outputData);
}
