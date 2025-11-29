package use_case.ratemyprof;

/**
 * Input Boundary for RateMyProf Use Case.
 */
public interface RateMyProfInputBoundary {

    /**
     * Executes the RateMyProf use case.
     * @param profInputData the input data for the Rate My Prof Use Case.
     */
    void execute(RateMyProfInputData profInputData);
}
