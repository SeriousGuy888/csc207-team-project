package use_case;

public interface WalkTimeOutputBoundary {

    /**
     * Called when the walk time is successfully calculated.
     * @param output The data packet containing the time (in minutes).
     */
    void prepareSuccessView(WalkTimeOutputData output);

    /**
     * Called when the calculation fails (Alternate Flow).
     * @param error The error message to display (e.g., "Walk time unavailable").
     */
    void prepareFailView(String error);
}
