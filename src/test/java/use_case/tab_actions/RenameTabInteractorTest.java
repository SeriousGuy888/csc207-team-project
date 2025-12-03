package use_case.tab_actions;

import entity.Timetable;
import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.WorkbookDataAccessInterface;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.rename_tab.RenameTabOutputBoundary;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RenameTabInteractorTest {

    @Test
    void executeSuccess() {
        // Arrange
        Timetable t1 = new Timetable();
        t1.setTimetableName("Old Name");
        List<Timetable> list = new ArrayList<>();
        list.add(t1);
        Workbook workbook = new Workbook(list);

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { /* Mock save */ }
        };

        RenameTabOutputBoundary presenter = wb -> {
            assertEquals("New Name", wb.getTimetables().get(0).getTimetableName());
        };

        RenameTabInteractor interactor = new RenameTabInteractor(dao, presenter);

        // Act
        interactor.execute(0, "New Name");

        // Assert
        assertEquals("New Name", t1.getTimetableName());
    }

    @Test
    void executeIndexTooLarge() {
        // Arrange
        Workbook workbook = new Workbook(new ArrayList<>()); // Empty, size 0

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { fail("Should not save"); }
        };

        RenameTabOutputBoundary presenter = wb -> fail("Should not present");

        RenameTabInteractor interactor = new RenameTabInteractor(dao, presenter);

        // Act
        interactor.execute(0, "New Name"); // 0 is >= size (0)
    }

    @Test
    void executeNegativeIndex() {
        // Arrange
        Timetable t1 = new Timetable();
        List<Timetable> list = new ArrayList<>();
        list.add(t1);
        Workbook workbook = new Workbook(list); // Size 1

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { fail("Should not save"); }
        };

        RenameTabOutputBoundary presenter = wb -> fail("Should not present");

        RenameTabInteractor interactor = new RenameTabInteractor(dao, presenter);

        // Act
        interactor.execute(-1, "New Name"); // Negative index
    }
}