package use_case.save_workbook;

import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import data_access.workbook_persistence.InMemoryWorkbookDataAccessObject;
import data_access.workbook_persistence.strategies.GsonWorkbookSerialiser;
import entity.*;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SaveWorkbookInteractorTest {
    @Test
    void doTheThing() {
//        InMemoryWorkbookDataAccessObject dao = new InMemoryWorkbookDataAccessObject();
        FileWorkbookDataAccessObject dao = new FileWorkbookDataAccessObject();
        GsonWorkbookSerialiser serialiser = new GsonWorkbookSerialiser();
        SaveWorkbookPresenter presenter = new SaveWorkbookPresenter();
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(dao, presenter, serialiser);


        Timetable timetable = new Timetable();
        Section section = new Section(
                new CourseOffering(
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

//        Workbook deserialisedWorkbook;
//        try {
//            deserialisedWorkbook = serialiser.deserialise(dao.load(destination));
//        } catch (IOException e) {
//            System.out.println(":( " + e);
//            return;
//        }
//
//        System.out.println(workbook);
//        System.out.println(deserialisedWorkbook);
    }
}