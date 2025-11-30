package DisplayCourseDetailsTests;

import entity.Professor;
import org.junit.jupiter.api.Test;
import use_case.display_course_context.*;
import use_case.ratemyprof.RateMyProfInputBoundary;
import use_case.ratemyprof.RateMyProfInputData;
import use_case.ratemyprof.RateMyProfOutputData;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// --- MOCK DEPENDENCIES (Implementations for Interfaces) ---

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

        // Create mock data that the Interactor will process
        DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                expectedProfName, 0.0, 0.0, null
        );

        // *** CHANGE 1: DisplaySectionDetails constructor updated to remove time/location ***
        DisplaySectionDetails section = new DisplaySectionDetails(
                "LEC 01", // Display section name
                placeholderProf
        );

        return new DisplayCourseDetails(
                "Test Course Title",
                "Test Description",
                List.of(section)
        );
    }

    @Override
    public String getProfessorNameBySectionId(String sectionId) {
        // Not directly used in this test, but must be implemented
        return "";
    }
}

// MOCK RMP INTERACTOR: Simulates fetching RMP data synchronously
class MockRateMyProfInteractor implements RateMyProfInputBoundary {

    @Override
    public void execute(RateMyProfInputData profInputData) { /* Not used in this interactor */ }

    @Override
    public RateMyProfOutputData executeSynchronous(RateMyProfInputData profInputData) {
        String firstName = profInputData.getFirstName();
        String lastName = profInputData.getLastName();

        // NOTE: Adjusted Professor constructor to match the fields provided in your previous mock:
        return new RateMyProfOutputData(
                new Professor(
                        firstName,
                        lastName,
                        4.5, // <- Known AvgRating
                        106, // <- Known AvgDifficultyRating
                        3.2,
                        "Computer Science department",
                        "http://test.rmp/link" // <- Known Link
                        // (Assuming numRatings and department fields are handled internally by the Professor entity)
                )
        );
    }
}

// MOCK PRESENTER: Captures the final OutputData sent by the Interactor
class MockPresenter implements DisplayCourseDetailsOutputBoundary {
    private DisplayCourseDetailsOutputData successOutput;
    private String failMessage;

    @Override
    public void prepareSuccessView(DisplayCourseDetailsOutputData outputData) {
        this.successOutput = outputData; // Capture the success data
    }

    @Override
    public void prepareFailView(String error) {
        this.failMessage = error; // Capture the failure message
    }

    public DisplayCourseDetailsOutputData getSuccessOutput() { return successOutput; }
    public String getFailMessage() { return failMessage; }
}


// --- THE UNIT TEST CLASS ---

public class DisplayCourseDetailsInteractorTest {

    private final String TEST_COURSE_ID = "CSC207";
    private final String PROF_FULL_NAME = "Paul Gries";
    private final String PROF_FIRST_NAME = "Paul";
    private final String PROF_LAST_NAME = "Gries";

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

        // Act
        interactor.execute(inputData);

        // Assert
        // 1. Check if success view was prepared
        assertNotNull(mockPresenter.getSuccessOutput(), "Presenter should have received a success view.");

        // 2. Check the final course details
        DisplayCourseDetails finalDetails = mockPresenter.getSuccessOutput().getCourseDetails();
        assertEquals("Test Course Title", finalDetails.getCourseTitle(), "Course title should match DAO output.");

        // 3. Check that the professor details were enriched with RMP data
        DisplaySectionDetails finalSection = finalDetails.getSections().get(0);
        DisplayProfessorDetails finalProf = finalSection.getProfessorDetails();

        // Check that the section name is correct
        assertEquals("LEC 01", finalSection.getSectionName(), "Section name should be the display name provided by the DAO.");

        // Check that the professor details contain the known RMP rating and link
        assertEquals(PROF_FIRST_NAME + " " + PROF_LAST_NAME, finalProf.getName(), "Full name should be correctly reconstructed (Paul Gries).");
        assertEquals(4.5, finalProf.getAvgRating(), 0.01, "Rating should match the mocked RMP output (4.5).");
        assertEquals(3.2, finalProf.getAvgDifficultyRating(), 0.01, "Difficulty should match the mocked RMP output (3.2).");
        assertEquals("http://test.rmp/link", finalProf.getLink(), "Link should match the mocked RMP output.");
    }

    // Test 2: Execution fails when the DAO returns null (Course Not Found)
    @Test
    void execute_shouldPrepareFailView_WhenDAOreturnsNull() {
        // Arrange
        MockPresenter mockPresenter = new MockPresenter();

        // DAO returns NO data (false)
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
        // Check if fail view was prepared with the correct message
        assertNull(mockPresenter.getSuccessOutput(), "Presenter should not have received a success view.");
        assertTrue(mockPresenter.getFailMessage().contains("could not be found"), "Presenter should receive a failure message.");
    }
}