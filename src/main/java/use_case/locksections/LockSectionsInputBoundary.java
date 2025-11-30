package use_case.locksections;

/**
 * Input boundary for the Lock Sections use case.
 */
public interface LockSectionsInputBoundary {

    /**
     * Executes the Lock Sections use case.
     *
     * @param inputData the sections that should be locked after this call
     */
    void execute(LockSectionsInputData inputData);
}
