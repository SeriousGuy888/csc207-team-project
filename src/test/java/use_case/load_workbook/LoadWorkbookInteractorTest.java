package use_case.load_workbook;

import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import interface_adapter.load_workbook.LoadWorkbookPresenter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LoadWorkbookInteractorTest {
    @Test
    void doTheThing() {
        FileWorkbookDataAccessObject dao = new FileWorkbookDataAccessObject(List.of("courses/sample_data.json"));
        LoadWorkbookPresenter presenter = new LoadWorkbookPresenter();
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(dao, presenter);

        Path source = Paths.get("sandbox", "workbook.json");
        LoadWorkbookInputData inputData = new LoadWorkbookInputData(source);

        interactor.execute(inputData);
    }
}
