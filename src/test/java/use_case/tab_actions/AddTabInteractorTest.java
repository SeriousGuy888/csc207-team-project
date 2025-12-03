package use_case.tab_actions;

import entity.Timetable;
import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.WorkbookDataAccessInterface;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.add_tab.AddTabOutputBoundary;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AddTabInteractorTest {

    @Test
    void executeAddsFirstTimetable() {
        // Arrange
        Workbook workbook = new Workbook(new ArrayList<>());

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { /* verify save */ }
        };

        AddTabOutputBoundary presenter = wb -> {
            assertEquals(1, wb.getTimetables().size());
            assertEquals("Timetable 1", wb.getTimetables().get(0).getTimetableName());
        };

        AddTabInteractor interactor = new AddTabInteractor(dao, presenter);

        // Act
        interactor.execute();
    }

    @Test
    void executeAddsSecondTimetable() {
        // Arrange
        Workbook workbook = new Workbook(new ArrayList<>());
        // Pre-populate with one tab
        Timetable t1 = new Timetable();
        t1.setTimetableName("Timetable 1");
        workbook.addTimetable(t1);

        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { /* verify save */ }
        };

        AddTabOutputBoundary presenter = wb -> {
            // Expect 2 items now
            assertEquals(2, wb.getTimetables().size());
            // Check the NEW item is named correctly
            assertEquals("Timetable 2", wb.getTimetables().get(1).getTimetableName());
        };

        AddTabInteractor interactor = new AddTabInteractor(dao, presenter);

        // Act
        interactor.execute();
    }
}
