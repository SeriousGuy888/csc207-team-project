package use_case.save_workbook;

import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;

public class SaveWorkbookInteractor implements SaveWorkbookInputBoundary {
    private final SaveWorkbookDataAccessInterface dataAccessObject;
    private final SaveWorkbookOutputBoundary presenter;

    public SaveWorkbookInteractor(SaveWorkbookDataAccessInterface dataAccessObject,
                                  SaveWorkbookOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    public void execute(SaveWorkbookInputData inputData) {
        Workbook workbook = inputData.getWorkbook();
        Path destination = inputData.getDestination();

        try {
            dataAccessObject.save(workbook, destination);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        SaveWorkbookOutputData outputData = new SaveWorkbookOutputData(destination);
        presenter.prepareSuccessView(outputData);
    }
}