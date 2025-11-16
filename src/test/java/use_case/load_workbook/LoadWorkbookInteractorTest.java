package use_case.load_workbook;

import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import data_access.workbook_persistence.strategies.GsonWorkbookSerialiser;
import entity.*;
import interface_adapter.load_workbook.LoadWorkbookPresenter;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import org.junit.jupiter.api.Test;
import use_case.save_workbook.SaveWorkbookInputData;
import use_case.save_workbook.SaveWorkbookInteractor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LoadWorkbookInteractorTest {
    @Test
    void doTheThing() {
        FileWorkbookDataAccessObject dao = new FileWorkbookDataAccessObject();
        GsonWorkbookSerialiser serialiser = new GsonWorkbookSerialiser();
        LoadWorkbookPresenter presenter = new LoadWorkbookPresenter();
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(dao, presenter, serialiser);

        Path source = Paths.get("sandbox", "workbook.json");
        LoadWorkbookInputData inputData = new LoadWorkbookInputData(source);

        interactor.execute(inputData);
    }
}
