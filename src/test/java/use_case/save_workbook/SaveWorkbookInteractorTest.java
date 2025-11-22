package use_case.save_workbook;

import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import entity.*;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SaveWorkbookInteractorTest {
    @Test
    void doTheThing() {
        FileWorkbookDataAccessObject dao = new FileWorkbookDataAccessObject(List.of("courses/sample_data.json"));
        SaveWorkbookPresenter presenter = new SaveWorkbookPresenter();
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(dao, presenter);


        Timetable timetable = new Timetable();
        Section section = new Section(
                new CourseOffering(
                        "MAT237Y1-F-20259",
                        new CourseCode("MAT137Y1"),
                        "Pain and Agony",
                        "two semesters of it"
                ),
                "LEC0101",
                Section.TeachingMethod.LECTURE
        );
        section.addMeeting(new Meeting(
                new UofTLocation("MY", "150"),
                new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.THURSDAY,
                        1000 * 60 * 60 * 13,
                        1000 * 60 * 60 * 15)
        ));

        timetable.addSection(section);
        Workbook workbook = new Workbook(List.of(timetable));
        Path destination = Paths.get("sandbox", "workbook.json");
        SaveWorkbookInputData inputData = new SaveWorkbookInputData(workbook, destination);


        interactor.execute(inputData);
    }
}