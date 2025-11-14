package use_case.save_workbook;

import com.google.gson.Gson;
import entity.*;

import java.util.List;

public class SaveWorkbookInteractor {
    private final Gson gson = new Gson();

    public void execute() {
        Timetable timetable = new Timetable();
        timetable.addSection(new Section(
                new CourseOffering(
                        new CourseCode("MAT137Y1"),
                        "Pain",
                        "Agony"
                ),
                "LEC0101",
                Section.TeachingMethod.LECTURE
        ));
        Workbook workbook = new Workbook(List.of(timetable));

        String json = gson.toJson(workbook);
        System.out.println(json);
    }
}