package use_case.tab_actions;

import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.WorkbookDataAccessInterface;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.add_tab.AddTabOutputBoundary;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AddTabInteractorTest {

    @Test
    void executeAddsTimetableAndSaves() {
        // 1. Arrange
        // Create a simple in-memory workbook
        Workbook workbook = new Workbook(new ArrayList<>());

        // Create a stub DAO that holds this workbook
        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() {
                return workbook;
            }

            @Override
            public void saveWorkbook(Workbook wb) {
                // Verify the workbook passed to save is the same instance
                assertSame(workbook, wb);
            }
        };

        // Create a stub Presenter to capture the output
        AddTabOutputBoundary presenter = new AddTabOutputBoundary() {
            @Override
            public void prepareSuccessView(Workbook wb) {
                // Assertions for the success view
                assertEquals(1, wb.getTimetables().size());
                assertEquals("Timetable 1", wb.getTimetables().get(0).getTimetableName());
            }
        };

        AddTabInteractor interactor = new AddTabInteractor(dao, presenter);

        // 2. Act
        interactor.execute();

        // 3. Assert (Post-execution checks)
        // Check that a second call increments the number correctly
        interactor.execute();
        assertEquals(2, workbook.getTimetables().size());
        assertEquals("Timetable 2", workbook.getTimetables().get(1).getTimetableName());
    }
}
