package use_case.osrm_walktime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Ensure these imports match your file structure exactly
import use_case.osrm_walktime.WalkTimeDataAccessInterface.ApiConnectionException;
import use_case.osrm_walktime.WalkTimeDataAccessInterface.InvalidLocationException;

class WalkTimeInteractorTest {

    @Test
    void successTest() {
        // 1. Arrange: Manual Mock of DAO that returns a success value (500)
        WalkTimeDataAccessInterface successDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) {
                return 500;
            }
        };

        // Manual Mock of Presenter to verify the output data
        WalkTimeOutputBoundary successPresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                // VERIFICATION: Check if the duration extracted is correct
                assertEquals(500, output.getDurationInSeconds());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Success test failed unexpectedly with error: " + error);
            }
        };

        // Initialize Interactor
        WalkTimeInteractor interactor = new WalkTimeInteractor(successDao, successPresenter);
        WalkTimeInputData input = new WalkTimeInputData("BA", "MY");

        // 2. Act
        interactor.execute(input);
    }

    @Test
    void failureInvalidLocationTest() {
        // 1. Arrange: Manual Mock of DAO that throws InvalidLocationException
        WalkTimeDataAccessInterface failureDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) throws InvalidLocationException {
                // Note: Your Exception class adds "Unknown Building: " automatically
                throw new InvalidLocationException("BAD_CODE");
            }
        };

        // Manual Mock of Presenter to verify the error message
        WalkTimeOutputBoundary failurePresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                fail("Expected failure but got success");
            }

            @Override
            public void prepareFailView(String error) {
                // VERIFICATION:
                // Interactor adds: "Invalid Building: "
                // Exception adds: "Unknown Building: "
                // Code is: "BAD_CODE"
                assertEquals("Invalid Building: Unknown Building: BAD_CODE", error);
            }
        };

        WalkTimeInteractor interactor = new WalkTimeInteractor(failureDao, failurePresenter);
        WalkTimeInputData input = new WalkTimeInputData("BAD_CODE", "MY");

        // 2. Act
        interactor.execute(input);
    }

    @Test
    void failureNetworkErrorTest() {
        // 1. Arrange: Manual Mock of DAO that throws ApiConnectionException
        WalkTimeDataAccessInterface networkFailDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) throws ApiConnectionException {
                throw new ApiConnectionException("Timeout");
            }
        };

        // Manual Mock of Presenter to verify the error message
        WalkTimeOutputBoundary networkFailPresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                fail("Expected network failure but got success");
            }

            @Override
            public void prepareFailView(String error) {
                // VERIFICATION:
                // Interactor adds: "Network Error: "
                // Exception msg is: "Timeout"
                assertEquals("Network Error: Timeout", error);
            }
        };

        WalkTimeInteractor interactor = new WalkTimeInteractor(networkFailDao, networkFailPresenter);
        WalkTimeInputData input = new WalkTimeInputData("BA", "MY");

        // 2. Act
        interactor.execute(input);
    }
}