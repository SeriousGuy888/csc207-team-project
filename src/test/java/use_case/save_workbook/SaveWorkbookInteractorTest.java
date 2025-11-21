package use_case.save_workbook;

import data_access.AvailableCoursesDataAccessObject;
import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import entity.*;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import org.junit.jupiter.api.Test;
import use_case.TestConstants;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SaveWorkbookInteractorTest {
    @Test
    void doTheThing() {
        FileWorkbookDataAccessObject dao = new FileWorkbookDataAccessObject(
                new AvailableCoursesDataAccessObject(List.of()));
        SaveWorkbookPresenter presenter = new SaveWorkbookPresenter();
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(dao, presenter);


        Timetable timetable = new Timetable();
        TestConstants.COURSE_OFFERING_MAT137.getAvailableSections().forEach(timetable::addSection);

        Workbook workbook = new Workbook(List.of(timetable));
        Path destination = Paths.get("sandbox", "workbook.json");
        SaveWorkbookInputData inputData = new SaveWorkbookInputData(workbook, destination);


        interactor.execute(inputData);
    }
}