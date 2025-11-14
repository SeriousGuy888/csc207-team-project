package use_case.save_workbook;

import data_access.workbook_persistence.strategies.WorkbookSerialiser;
import entity.Workbook;

import java.nio.file.Path;

public class SaveWorkbookInteractor {
    private final SaveWorkbookDataAccessInterface dataAccessObject;
    private final WorkbookSerialiser workbookSerialiser;

    public SaveWorkbookInteractor(SaveWorkbookDataAccessInterface dataAccessObject,
                                  WorkbookSerialiser workbookSerialiser) {
        this.dataAccessObject = dataAccessObject;
        this.workbookSerialiser = workbookSerialiser;
    }

    public void execute(SaveWorkbookInputData inputData) {
        Workbook workbook = inputData.getWorkbook();
        Path destination = inputData.getDestination();
        String serialised = workbookSerialiser.serialise(workbook);
        dataAccessObject.save(serialised, destination);
    }
}