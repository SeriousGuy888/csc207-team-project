package use_case.osrm_walktime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WalkTimeInteractorTest {

    @Test
    void successTest() {
        WalkTimeDataAccessInterface successDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) {
                return 500;
            }
        };

        WalkTimeOutputBoundary successPresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                assertEquals(500, output.getDurationInSeconds());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Success test failed unexpectedly with error: " + error);
            }
        };

        WalkTimeInteractor interactor = new WalkTimeInteractor(successDao, successPresenter);
        WalkTimeInputData input = new WalkTimeInputData("BA", "MY");

        interactor.execute(input);
    }

    @Test
    void failureInvalidLocationTest() {
        WalkTimeDataAccessInterface failureDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) throws InvalidLocationException {
                throw new InvalidLocationException("BAD_CODE");
            }
        };

        WalkTimeOutputBoundary failurePresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                fail("Expected failure but got success");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Invalid Building: Unknown Building: BAD_CODE", error);
            }
        };

        WalkTimeInteractor interactor = new WalkTimeInteractor(failureDao, failurePresenter);
        WalkTimeInputData input = new WalkTimeInputData("BAD_CODE", "MY");

        interactor.execute(input);
    }

    @Test
    void failureNetworkErrorTest() {
        WalkTimeDataAccessInterface networkFailDao = new WalkTimeDataAccessInterface() {
            @Override
            public int getWalkTimeInSeconds(String start, String end) throws ApiConnectionException {
                throw new ApiConnectionException("Timeout");
            }
        };

        WalkTimeOutputBoundary networkFailPresenter = new WalkTimeOutputBoundary() {
            @Override
            public void prepareSuccessView(WalkTimeOutputData output) {
                fail("Expected network failure but got success");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Network Error: Timeout", error);
            }
        };

        WalkTimeInteractor interactor = new WalkTimeInteractor(networkFailDao, networkFailPresenter);
        WalkTimeInputData input = new WalkTimeInputData("BA", "MY");

        interactor.execute(input);
    }
}
