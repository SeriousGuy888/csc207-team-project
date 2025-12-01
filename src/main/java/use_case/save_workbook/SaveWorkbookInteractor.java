package use_case.save_workbook;

import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;

public class SaveWorkbookInteractor implements SaveWorkbookInputBoundary {
    private final WorkbookDataAccessInterface workbookDataAccessObject;
    private final SaveWorkbookDataAccessInterface fileSavingDataAccessObject;
    private final SaveWorkbookOutputBoundary presenter;

    public SaveWorkbookInteractor(WorkbookDataAccessInterface workbookDataAccessObject,
                                  SaveWorkbookDataAccessInterface fileSavingDataAccessObject,
                                  SaveWorkbookOutputBoundary presenter) {
        this.workbookDataAccessObject = workbookDataAccessObject;
        this.fileSavingDataAccessObject = fileSavingDataAccessObject;
        this.presenter = presenter;
    }

    public void execute(SaveWorkbookInputData inputData) {
        final Workbook workbook = workbookDataAccessObject.getWorkbook();
        final Path destination = inputData.getDestination();

        try {
            fileSavingDataAccessObject.save(workbook, destination);
        } catch (IOException | RuntimeException ex) {
            presenter.prepareFailView("An problem occurred while saving workbook."
                    + " Check the path and try again: " + ex);
            return;
        }

        SaveWorkbookOutputData outputData = new SaveWorkbookOutputData(destination);
        presenter.prepareSuccessView(outputData);
    }
}