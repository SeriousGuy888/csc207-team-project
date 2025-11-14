package use_case.save_workbook;

import data_access.workbook_persistence.strategies.WorkbookSerialiser;
import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;

public class SaveWorkbookInteractor implements SaveWorkbookInputBoundary {
    private final SaveWorkbookDataAccessInterface dataAccessObject;
    private final SaveWorkbookOutputBoundary presenter;

    private final WorkbookSerialiser workbookSerialiser;

    public SaveWorkbookInteractor(SaveWorkbookDataAccessInterface dataAccessObject,
                                  SaveWorkbookOutputBoundary presenter,
                                  WorkbookSerialiser workbookSerialiser) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
        this.workbookSerialiser = workbookSerialiser;
    }

    public void execute(SaveWorkbookInputData inputData) {
        Workbook workbook = inputData.getWorkbook();
        Path destination = inputData.getDestination();
        String serialised = workbookSerialiser.serialise(workbook);

        try {
            dataAccessObject.save(serialised, destination);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
        }

        SaveWorkbookOutputData outputData = new SaveWorkbookOutputData(destination);
        presenter.prepareSuccessView(outputData);
    }
}