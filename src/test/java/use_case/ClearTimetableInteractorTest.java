package use_case;

import entity.CourseOffering;
import entity.Section;
import entity.Timetable;
import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.clear_timetable.ClearTimetableInteractor;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClearTimetableInteractorTest {

    @Test
    void executeClearsTimetableAndUpdatesView() {
        // Arrange
        Timetable t1 = new Timetable();
        Section someSection = new Section(
                CourseOffering.createUnknownCourseOffering("CSC207H1"),
                "Dummy Section",
                Section.TeachingMethod.LECTURE);
        t1.addSection(someSection);

        List<Timetable> list = new ArrayList<>();
        list.add(t1);
        Workbook workbook = new Workbook(list);

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { /* success */ }
        };

        final boolean[] successCalled = {false};
        TimetableUpdateOutputBoundary presenter = new TimetableUpdateOutputBoundary() {
            @Override
            public void prepareSuccessView(TimetableUpdateOutputData data) {
                successCalled[0] = true;
                assertEquals(0, data.getTabIndex());
                assertSame(t1, data.getTimetable());
                // Verify it's empty (if you populated it before)
                assertTrue(data.getTimetable().getSections().isEmpty());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail");
            }
        };

        ClearTimetableInteractor interactor = new ClearTimetableInteractor(dao, presenter);

        // Act
        interactor.execute(0); // Clear index 0

        // Assert
        assertTrue(successCalled[0]);
    }

    @Test
    void executeInvalidIndexDoesNothing() {
        // Arrange
        Workbook workbook = new Workbook(new ArrayList<>()); // Empty
        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { fail("Should not save"); }
        };

        TimetableUpdateOutputBoundary presenter = new TimetableUpdateOutputBoundary() {
            @Override
            public void prepareSuccessView(TimetableUpdateOutputData data) { fail("Should not present"); }
            @Override
            public void prepareFailView(String error) { fail("Should not fail"); }
        };

        ClearTimetableInteractor interactor = new ClearTimetableInteractor(dao, presenter);

        // Act
        interactor.execute(0); // Index 0 invalid for empty list
    }
}
