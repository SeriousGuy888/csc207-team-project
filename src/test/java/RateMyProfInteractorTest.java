import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Import classes from use_case.ratemyprof package and professor entity
import use_case.ratemyprof.*;
import entity.Professor;

class RateMyProfInteractorTest {

    // Mock Data Access Object for Testing:
    // This allows to control the interactor's dependencies
    private static class MockRateMyProfDataAccess implements RateMyProfDataAccessInterface {
        private final Professor mockProfessor;
        private final boolean shouldThrowException;
        public boolean getProfessorInfoCalled = false;
        public String lastFirstName = null;
        public String lastLastName = null;

        public MockRateMyProfDataAccess(Professor professor, boolean shouldThrowException) {
            this.mockProfessor = professor;
            this.shouldThrowException = shouldThrowException;
        }

        @Override
        public Professor getProfessorInfo(String profFirstName, String profLastName) {
            getProfessorInfoCalled = true;
            lastFirstName = profFirstName;
            lastLastName = profLastName;

            if (shouldThrowException) {
                // Mock exception that the real DAO might throw
                throw new RuntimeException("Mock Error: Could not fetch professor info.");
            }
            return mockProfessor;
        }
    }

    // This allows to verify the interactor signaled success or failure.
    private static class MockRateMyProfPresenter implements RateMyProfOutputBoundary {
        public boolean successViewCalled = false;
        public boolean failViewCalled = false;
        public RateMyProfOutputData receivedOutputData = null;
        public String receivedErrorMessage = null;

        @Override
        public void prepareSuccessView(RateMyProfOutputData outputData) {
            this.successViewCalled = true;
            this.receivedOutputData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failViewCalled = true;
            this.receivedErrorMessage = error;
        }
    }

    //  Test Cases:

    @Test
    void testExecute_Success() {
        // Input Data and Expected Output
        String firstName = "Paul";
        String lastName = "Gries";
        RateMyProfInputData inputData = new RateMyProfInputData(firstName, lastName);

        // expected responses
        double expectedAvgRating = 4.4;
        int expectedNumRatings = 106;
        double expectedDifficulty = 2.7;
        // Mock Professor object that the DAO will return
        Professor mockProf = new Professor("Paul", "Gries", 4.4, 106,
                2.7, "Computer Science department", "https://www.ratemyprofessors.com/professor/30803");

        // Mock DAO, presenter, and Interactor
        MockRateMyProfPresenter mockRateMyProfOutputBoundary = new MockRateMyProfPresenter();
        MockRateMyProfDataAccess mockDAO = new MockRateMyProfDataAccess(mockProf, false);
        RateMyProfInteractor interactor = new RateMyProfInteractor(mockDAO, mockRateMyProfOutputBoundary);

        // Execute the Use Case
        // Tests if it successfully calls the DAO with the correct data.

        interactor.execute(inputData);

        // Assertions!

        // Verify the Interactor signaled success
        assertTrue(mockRateMyProfOutputBoundary.successViewCalled, "Interactor must call prepareSuccessView.");

        // Get the data object the Interactor handed to the Presenter
        RateMyProfOutputData actualOutputData = mockRateMyProfOutputBoundary.receivedOutputData;

        assertNotNull(actualOutputData, "Output Data should not be null on success.");

        // Verify the specific rating values match the original mock Professor object
        assertEquals(expectedAvgRating, actualOutputData.getavgRating(), 0.001,
                "The Interactor passed the wrong average rating to the Presenter.");

        assertEquals(expectedNumRatings, actualOutputData.getnumRatings(),
                "The Interactor passed the wrong number of ratings to the Presenter.");

        assertEquals(expectedDifficulty, actualOutputData.getavgDifficultyRating(), 0.001,
                "The Interactor passed the wrong difficulty rating to the Presenter.");
        // Verify that the interactor called the DAO method
        assertTrue(mockDAO.getProfessorInfoCalled, "The interactor should have called getProfessorInfo.");

        // Verify that the interactor passed the correct arguments to the DAO
        assertEquals(firstName, mockDAO.lastFirstName, "The interactor passed the wrong first name.");
        assertEquals(lastName, mockDAO.lastLastName, "The interactor passed the wrong last name.");

    }

    @Test
    void testExecute_Failure() {
        // Input Data
        RateMyProfInputData inputData = new RateMyProfInputData("Bad", "Prof");

        // Setup Mock DAO and presenter to throw an exception
        MockRateMyProfPresenter mockProfPresenter = new MockRateMyProfPresenter();
        MockRateMyProfDataAccess mockDAO = new MockRateMyProfDataAccess(null, true);
        RateMyProfInteractor interactor = new RateMyProfInteractor(mockDAO, mockProfPresenter);

        // Execute (The Interactor handles the exception internally)
        interactor.execute(inputData);

        // Assertions!

        // Verify the call was attempted
        assertTrue(mockDAO.getProfessorInfoCalled, "The interactor should have attempted the DAO call.");

        // Verify the failure signal was sent to the Presenter (This is the key test!)
        assertTrue(mockProfPresenter.failViewCalled, "The interactor must signal failure to the Presenter.");
        assertFalse(mockProfPresenter.successViewCalled, "The interactor must NOT signal success on failure.");

        // Verify the error message is present
        assertNotNull(mockProfPresenter.receivedErrorMessage, "The Presenter must receive an error message.");
    }
}