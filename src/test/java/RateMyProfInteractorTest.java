import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Import classes from use_case.ratemyprof package and professor entity
import use_case.ratemyprof.RateMyProfDataAccessInterface;
import use_case.ratemyprof.RateMyProfInputData;
import use_case.ratemyprof.RateMyProfInteractor;
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

    // --- Test Cases ---

    @Test
    void testExecute_Success() {
        // Input Data and Expected Output
        String firstName = "Paul";
        String lastName = "Gries";
        RateMyProfInputData inputData = new RateMyProfInputData(firstName, lastName);

        // Mock Professor object that the DAO will return
        Professor mockProf = new Professor("Paul", "Gries", 4.4, 106,
                2.7, "Computer Science department", "https://www.ratemyprofessors.com/professor/30803");

        // Mock DAO and Interactor
        MockRateMyProfDataAccess mockDAO = new MockRateMyProfDataAccess(mockProf, false);
        RateMyProfInteractor interactor = new RateMyProfInteractor(mockDAO);

        // Execute the Use Case
        // Tests if it successfully calls the DAO with the correct data.

        // TODO: Pass a presenter here to verify the output data was passed back to the UI.
        //  For now, testing the DAO interaction.
        interactor.execute(inputData);

        // Assertions!
        // Verify that the interactor called the DAO method
        assertTrue(mockDAO.getProfessorInfoCalled, "The interactor should have called getProfessorInfo.");

        // Verify that the interactor passed the correct arguments to the DAO
        assertEquals(firstName, mockDAO.lastFirstName, "The interactor passed the wrong first name.");
        assertEquals(lastName, mockDAO.lastLastName, "The interactor passed the wrong last name.");

        // TODO: Once I add logic to interactor.execute(), add more assertions here to check for
        //  successful output or other state changes.
    }

    @Test
    void testExecute_Failure() {
        // Input Data
        RateMyProfInputData inputData = new RateMyProfInputData("Bad", "Prof");

        // Setup Mock DAO to throw an exception
        MockRateMyProfDataAccess mockDAO = new MockRateMyProfDataAccess(null, true);
        RateMyProfInteractor interactor = new RateMyProfInteractor(mockDAO);

        // Execute and Assert Exception Handling

        // For now, we'll simply verify the DAO call happens, but be aware that
        // a real test would verify the interactor handles the exception gracefully.

        // We'll wrap the call in a try/catch or use an assertThrows IF the interactor 
        // is designed to propagate the exception.
        try {
            interactor.execute(inputData);
            // If the execute were to call the DAO, and the DAO throws, 
            // the interactor needs proper try/catch or the test should use assertThrows
        } catch (RuntimeException e) {
            // If the interactor is intended to re-throw the DAO's exception,
            // this block would verify the type of exception thrown.
        }

        // Still verify the call was attempted, even if it failed
        assertTrue(mockDAO.getProfessorInfoCalled, "The interactor should have attempted the DAO call.");
    }
}