package use_case.ratemyprof;

import java.util.logging.Logger;

/**
 * A minimal implementation of the RMP Output Boundary used when the RMP Use Case 
 * is called synchronously by another interactor (e.g., DisplayCourseDetails). 
 * The reason the presenter is in use_case here is because this doesn't connect to UI directly.
 */
public class RateMyProfPresenter implements RateMyProfOutputBoundary {

    private static final Logger LOGGER = Logger.getLogger(RateMyProfPresenter.class.getName());

    @Override
    public void prepareSuccessView(RateMyProfOutputData output) {
        // Acknowledge success for logging/debugging, but no UI update needed.
        LOGGER.fine("RMP Interactor successfully returned data.");
    }

    @Override
    public void prepareFailView(String error) {
        // Log the failure, as the calling interactor (DisplayCourseDetails) 
        // should handle the failure gracefully by returning a default Professor.
        LOGGER.warning("RMP API Failure reported: " + error);
    }
}