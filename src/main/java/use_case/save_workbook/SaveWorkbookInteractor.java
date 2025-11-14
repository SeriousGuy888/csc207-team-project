package use_case.save_workbook;

import entity.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SaveWorkbookInteractor {
    public void execute() {
        // this is an experiment
        // based on this tutorial https://www.baeldung.com/java-serialization

        Timetable timetable = new Timetable();
        timetable.addSection(new Section(
                new CourseOffering(
                        new CourseCode("MAT137H1"),
                        "Pain",
                        "Agony"
                ),
                "LEC0101",
                Section.TeachingMethod.LECTURE
        ));
        Workbook workbook = new Workbook(List.of(timetable));

//        try {
//            FileOutputStream fos = new FileOutputStream("ice_cream.txt");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(workbook);
//            oos.flush();
//            oos.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        Workbook deserWorkbook;
        try {
            FileInputStream fis = new FileInputStream("ice_cream.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            deserWorkbook = (Workbook) ois.readObject();
            ois.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(workbook);
        System.out.println(deserWorkbook);
    }
}
