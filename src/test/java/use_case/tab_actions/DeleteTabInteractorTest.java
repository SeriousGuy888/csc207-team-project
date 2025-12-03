package use_case.tab_actions;

import entity.Timetable;
import entity.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.WorkbookDataAccessInterface;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabOutputBoundary;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTabInteractorTest {

    private Workbook workbook;
    private Timetable t1;
    private Timetable t2;

    @BeforeEach
    void setUp() {
        t1 = new Timetable();
        t1.setTimetableName("T1");
        t2 = new Timetable();
        t2.setTimetableName("T2");

        List<Timetable> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        workbook = new Workbook(list);
    }

    @Test
    void executeSuccess() {
        // Arrange
        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { /* Mock save */ }
        };

        DeleteTabOutputBoundary presenter = wb -> {
            assertEquals(1, wb.getTimetables().size());
            assertEquals("T2", wb.getTimetables().get(0).getTimetableName());
        };

        DeleteTabInteractor interactor = new DeleteTabInteractor(dao, presenter);

        // Act: Delete index 0 (T1)
        interactor.execute(0);

        // Assert
        assertEquals(1, workbook.getTimetables().size());
        assertFalse(workbook.getTimetables().contains(t1));
    }

    @Test
    void executeOutOfBoundsLow() {
        // Arrange
        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { fail("Should not save on invalid index"); }
        };

        DeleteTabOutputBoundary presenter = wb -> fail("Should not present success on invalid index");

        DeleteTabInteractor interactor = new DeleteTabInteractor(dao, presenter);

        // Act: Delete -1
        interactor.execute(-1);

        // Assert: Size unchanged
        assertEquals(2, workbook.getTimetables().size());
    }

    @Test
    void executeOutOfBoundsHigh() {
        // Arrange
        WorkbookDataAccessInterface dao = new WorkbookDataAccessInterface() {
            @Override
            public Workbook getWorkbook() { return workbook; }
            @Override
            public void saveWorkbook(Workbook wb) { fail("Should not save on invalid index"); }
        };

        DeleteTabOutputBoundary presenter = wb -> fail("Should not present success on invalid index");

        DeleteTabInteractor interactor = new DeleteTabInteractor(dao, presenter);

        // Act: Delete index 2 (size is 2, so max index is 1)
        interactor.execute(2);

        // Assert: Size unchanged
        assertEquals(2, workbook.getTimetables().size());
    }
}
