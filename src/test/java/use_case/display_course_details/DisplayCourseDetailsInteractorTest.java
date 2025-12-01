package use_case.display_course_details;

import entity.Professor;
import org.junit.jupiter.api.Test;
import use_case.display_course_context.*;
import use_case.ratemyprof.RateMyProfInputBoundary;
import use_case.ratemyprof.RateMyProfInputData;
import use_case.ratemyprof.RateMyProfOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                "LEC 01",                     // display section name
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
        // For this interactor test, the enrichment comes from RMP, not this lookup.
        return expectedProfName;
    }
}

// MOCK RMP INTERACTOR: Simulates fetching RMP data synchronously
class MockRateMyProfInteractor implements RateMyProfInputBoundary {

    @Override
    public void execute(RateMyProfInputData profInputData) {
        // Not used in this interactor test
    }

    @Override
    public RateMyProfOutputData executeSynchronous(RateMyProfInputData profInputData) {
        String firstName = profInputData.getFirstName();
        String lastName = profInputData.getLastName();

        // Construct a Professor with known values to assert against
        Professor prof = new Professor(
                firstName,
                lastName,
                4.5,                       // avgRating
                106,                       // numRatings
                3.2,                       // avgDifficulty
                "Computer Science department",        // department
                "http://test.rmp/link"     // link
        );
        return new RateMyProfOutputData(prof);
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


// --- THE UNIT TEST CLASS ---

public class DisplayCourseDetailsInteractorTest {

    private static final String TEST_COURSE_ID   = "CSC207H1";
    private static final String PROF_FULL_NAME   = "Paul Gries";
    private static final String PROF_FIRST_NAME  = "Paul";
    private static final String PROF_LAST_NAME   = "Gries";

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
        assertEquals("LEC 01", finalSection.getSectionName());

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
}
