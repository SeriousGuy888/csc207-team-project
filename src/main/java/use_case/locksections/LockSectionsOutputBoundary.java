package use_case.locksections;

/**
 * Output boundary for the Lock Sections use case.
 * Implemented by the presenter.
 */
public interface LockSectionsOutputBoundary {

    /**
     * Called when locking sections succeeds.
     */
    void prepareSuccessView(LockSectionsOutputData outputData);

    /**
     * Called when locking sections fails (e.g., invalid input).bgbg
     *
     * @param errorMessage a human-readable description of the error
     */
    void prepareFailView(String errorMessage);
}
