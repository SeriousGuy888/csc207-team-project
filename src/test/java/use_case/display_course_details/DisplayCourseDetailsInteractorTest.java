package use_case.display_course_details;

import entity.Professor;
import org.junit.jupiter.api.Test;
import use_case.display_course_context.*;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayCourseDetails;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayMeetingDetails;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayProfessorDetails;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplaySectionDetails;
import use_case.ratemyprof.RateMyProfInputBoundary;
import use_case.ratemyprof.RateMyProfInputData;
import use_case.ratemyprof.RateMyProfOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// MOCK DAO: Simulates fetching course data (with a placeholder prof name)
class MockCourseDetailsDAO implements DisplayCourseDetailsDataAccessInterface {

    private final String expectedProfName;
    private final boolean returnData;

    MockCourseDetailsDAO(String expectedProfName, boolean returnData) {
        this.expectedProfName = expectedProfName;
        this.returnData = returnData;
    }

    @Override
    public DisplayCourseDetails getCourseDetails(String courseId) {
        if (!returnData) {
            return null; // Simulates course not found (for fail test)
        }

        // Placeholder professor from DAO (before RMP enrichment)
        DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                expectedProfName,
                0.0,
                0.0,
                null
        );

        // Minimal dummy meeting/time/location for the test
        DisplayMeetingDetails mockTime = new DisplayMeetingDetails("MON", "10:00", "11:00", "BA");

        DisplaySectionDetails section = new DisplaySectionDetails(
                "LEC-0101",                     // display section name
                List.of(mockTime),             // arbitrary location
                placeholderProf               // professor before enrichment
        );

        return new DisplayCourseDetails(
                courseId,                     // courseId
                "Test Course Title",          // courseTitle
                "Test Description",           // courseDescription
                List.of(section)              // sections
        );
    }

    @Override
    public String getProfessorNameByCourseAndSection(String courseId, String sectionId) {
        return expectedProfName;
    }
}

// MOCK PRESENTER: Captures the final OutputData sent by the Interactor
class MockPresenter implements DisplayCourseDetailsOutputBoundary {
    private DisplayCourseDetailsOutputData successOutput;
    private String failMessage;

    @Override
    public void prepareSuccessView(DisplayCourseDetailsOutputData outputData) {
        this.successOutput = outputData;
    }

    @Override
    public void prepareFailView(String failedCourseId, String error) {
        this.failMessage =  failedCourseId + " - " + error;
    }

    public DisplayCourseDetailsOutputData getSuccessOutput() {
        return successOutput;
    }

    public String getFailMessage() {
        return failMessage;
    }
}

class MockRateMyProfInteractorFail implements RateMyProfInputBoundary {
    @Override
    public void execute(RateMyProfInputData profInputData) {
        // Not used
    }

    @Override
    public RateMyProfOutputData executeSynchronous(RateMyProfInputData profInputData) throws RuntimeException{
        // Force an exception to test the Interactor's catch block
        throw new RuntimeException("Mock RMP Interactor failed unexpectedly.");
    }
}


public class DisplayCourseDetailsInteractorTest {

    private static final String PROF_SINGLE_SPACE_NAME = " ";
    private static final String TEST_COURSE_ID   = "CSC207H1";
    private static final String PROF_FULL_NAME   = "Paul Gries";
    private static final String PROF_FIRST_NAME  = "Paul";
    private static final String PROF_LAST_NAME   = "Gries";
    private static final String PROF_SINGLE_NAME = "Cher";

    // Test 1: Successful execution with correct data enrichment
    @Test
    void execute_shouldPrepareSuccessView_WithCorrectlyEnrichedData() {
        // Arrange
        MockCourseDetailsDAO mockDao = new MockCourseDetailsDAO(PROF_FULL_NAME, true);
        MockRateMyProfInteractor mockRmpInteractor = new MockRateMyProfInteractor();
        MockPresenter mockPresenter = new MockPresenter();

        DisplayCourseDetailsInteractor interactor = new DisplayCourseDetailsInteractor(
                mockDao,
                mockPresenter,
                mockRmpInteractor
        );

        DisplayCourseDetailsInputData inputData = new DisplayCourseDetailsInputData(TEST_COURSE_ID);

        // execute
        interactor.execute(inputData);

        // Assert
        // 1. Presenter should have a success output
        assertNotNull(mockPresenter.getSuccessOutput(), "Presenter should have received a success view.");

        DisplayCourseDetailsOutputData outputData = mockPresenter.getSuccessOutput();
        DisplayCourseDetails finalDetails = outputData.getCourseDetails();

        //
        assertEquals(TEST_COURSE_ID, finalDetails.getCourseId());
        assertEquals("Test Course Title", finalDetails.getCourseTitle());
        assertEquals("Test Description", finalDetails.getCourseDescription());

        // 3. Check enriched professor details on the first section
        assertEquals(1, finalDetails.getSections().size());
        DisplaySectionDetails finalSection = finalDetails.getSections().get(0);
        DisplayProfessorDetails finalProf = finalSection.getProfessorDetails();

        // Section name should be what DAO provided
        assertEquals("LEC-0101", finalSection.getSectionName());

        // Prof data should come from RMP mock
        assertEquals(PROF_FIRST_NAME + " " + PROF_LAST_NAME, finalProf.getName());
        assertEquals(4.5, finalProf.getAvgRating(), 0.01);
        assertEquals(3.2, finalProf.getAvgDifficultyRating(), 0.01);
        assertEquals("http://test.rmp/link", finalProf.getLink());
    }

    // Test 2: Execution fails when the DAO returns null (Course Not Found)
    @Test
    void execute_shouldPrepareFailView_WhenDAOreturnsNull() {
        // Arrange
        MockPresenter mockPresenter = new MockPresenter();
        MockCourseDetailsDAO failDao = new MockCourseDetailsDAO(PROF_FULL_NAME, false);

        DisplayCourseDetailsInteractor interactor = new DisplayCourseDetailsInteractor(
                failDao,
                mockPresenter,
                new MockRateMyProfInteractor()
        );

        DisplayCourseDetailsInputData inputData = new DisplayCourseDetailsInputData("NON_EXISTENT");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(mockPresenter.getSuccessOutput(), "Success view should not be prepared when DAO returns null.");
        assertNotNull(mockPresenter.getFailMessage(), "Fail view should contain an error message.");
        assertTrue(
                mockPresenter.getFailMessage().toLowerCase().contains("could not be found"),
                "Failure message should mention that the course could not be found."
        );
    }

    // Test 3: RMP Interactor fails (e.g., API is down), Interactor returns default prof data
    @Test
    void execute_shouldPrepareSuccessView_WhenRMPFails() {
        // Arrange
        String placeholderProfName = "Jane Doe";
        MockCourseDetailsDAO mockDao = new MockCourseDetailsDAO(placeholderProfName, true);
        MockRateMyProfInteractorFail mockRmpFail = new MockRateMyProfInteractorFail(); // <-- Use the failing mock
        MockPresenter mockPresenter = new MockPresenter();

        DisplayCourseDetailsInteractor interactor = new DisplayCourseDetailsInteractor(
                mockDao,
                mockPresenter,
                mockRmpFail // Inject the failing dependency
        );

        DisplayCourseDetailsInputData inputData = new DisplayCourseDetailsInputData(TEST_COURSE_ID);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(mockPresenter.getSuccessOutput(), "Success view should still be prepared if RMP fails.");

        DisplayCourseDetails finalDetails = mockPresenter.getSuccessOutput().getCourseDetails();
        assertEquals(1, finalDetails.getSections().size());

        DisplayProfessorDetails finalProf = finalDetails.getSections().get(0).getProfessorDetails();

        // Check that the professor details are the default values from Professor.emptyProfessor()
        assertTrue(finalProf.getName().contains(Professor.emptyProfessor().getFirstName()));
        assertEquals(Professor.emptyProfessor().getAvgRating(), finalProf.getAvgRating(), 0.01);
        assertEquals(Professor.emptyProfessor().getAvgDifficultyRating(), finalProf.getAvgDifficultyRating(), 0.01);
        assertEquals(finalProf.getLink(), ""); // Link should be empty for failed lookup
    }

    class MockRateMyProfInteractor implements RateMyProfInputBoundary {
        private RateMyProfInputData lastInput; // <-- New field to capture input

        @Override
        public void execute(RateMyProfInputData profInputData) {
            // Not used in this interactor test
        }

        @Override
        public RateMyProfOutputData executeSynchronous(RateMyProfInputData profInputData) {
            this.lastInput = profInputData; // <-- Save the input
            String firstName = profInputData.getFirstName();
            String lastName = profInputData.getLastName();

            // Construct a Professor with known values to assert against
            Professor prof = new Professor(
                    firstName, // Use the input names here for clean testing
                    lastName,
                    4.5,
                    106,
                    3.2,
                    "Computer Science department",
                    "http://test.rmp/link"
            );
            return new RateMyProfOutputData(prof);
        }

        public RateMyProfInputData getLastInput() { // <-- Getter for verification
            return lastInput;
        }
    }

    @Test
    void execute_shouldHandleSingleNameProfessor() {
        MockCourseDetailsDAO mockDao = new MockCourseDetailsDAO(PROF_SINGLE_NAME, true);

        MockRateMyProfInteractor mockRmpInteractor = new MockRateMyProfInteractor();
        MockPresenter mockPresenter = new MockPresenter();

        DisplayCourseDetailsInteractor interactor = new DisplayCourseDetailsInteractor(
                mockDao,
                mockPresenter,
                mockRmpInteractor
        );

        DisplayCourseDetailsInputData inputData = new DisplayCourseDetailsInputData(TEST_COURSE_ID);

        interactor.execute(inputData);

        // Assert
        // 1. Verify the success output was prepared
        assertNotNull(mockPresenter.getSuccessOutput(), "Success view expected.");

        // 2. Verify the Interactor correctly extracted the names (firstName=Cher, lastName="")
        RateMyProfInputData rmpInput = mockRmpInteractor.getLastInput();
        assertNotNull(rmpInput, "RMP Interactor should have been executed.");
        assertEquals(PROF_SINGLE_NAME, rmpInput.getFirstName(),
                "First name should be the single name.");
        assertEquals("", rmpInput.getLastName(),
                "Last name should be empty for a single-name prof.");

        // 3. Verify the final professor object was constructed correctly based on the mock's return
        DisplayProfessorDetails finalProf = mockPresenter.getSuccessOutput()
                .getCourseDetails().getSections().get(0).getProfessorDetails();

        assertEquals(PROF_SINGLE_NAME + " ", finalProf.getName()); // Mock's constructor adds a space: "Cher" + " " + ""
        assertEquals(4.5, finalProf.getAvgRating(), 0.01);
        assertEquals(3.2, finalProf.getAvgDifficultyRating(), 0.01);
    }

    @Test
    void execute_shouldHandleSingleSpaceProfessorName() {
        MockCourseDetailsDAO mockDao = new MockCourseDetailsDAO(PROF_SINGLE_SPACE_NAME, true);
        MockRateMyProfInteractor mockRmpInteractor = new MockRateMyProfInteractor();
        MockPresenter mockPresenter = new MockPresenter();

        DisplayCourseDetailsInteractor interactor = new DisplayCourseDetailsInteractor(
                mockDao,
                mockPresenter,
                mockRmpInteractor
        );

        DisplayCourseDetailsInputData inputData = new DisplayCourseDetailsInputData(TEST_COURSE_ID);

        interactor.execute(inputData);

        // Assert
        assertNotNull(mockPresenter.getSuccessOutput(), "Success view expected.");

        RateMyProfInputData rmpInput = mockRmpInteractor.getLastInput();
        assertNotNull(rmpInput, "RMP Interactor should have been executed.");

        // verify that firstName and lastName were both set to the empty string ""
        // which happens when parts.length <= 0 and parts.length <= 1.
        assertEquals("", rmpInput.getFirstName(),
                "First name should be empty.");
        assertEquals("", rmpInput.getLastName(),
                "Last name should be empty.");
    }
}
