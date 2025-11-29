package use_case.ratemyprof;

/**
 * The Output Boundary for the RateMyProf Use Case.
 */
public interface RateMyProfOutputBoundary {
    /**
     * Prepares the success view for the RateMyProf Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(RateMyProfOutputData outputData);

    /**
     * Prepares the failure view for the RateMyProf Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

