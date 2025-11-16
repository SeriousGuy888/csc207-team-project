package use_case.load_workbook;

import data_access.workbook_persistence.strategies.WorkbookSerialiser;
import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

public class LoadWorkbookInteractor implements LoadWorkbookInputBoundary {
    private final LoadWorkbookDataAccessInterface dataAccessObject;
    private final LoadWorkbookOutputBoundary presenter;

    private final WorkbookSerialiser workbookSerialiser;


    public LoadWorkbookInteractor(LoadWorkbookDataAccessInterface dataAccessObject,
                                  LoadWorkbookOutputBoundary presenter,
                                  WorkbookSerialiser workbookSerialiser) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
        this.workbookSerialiser = workbookSerialiser;
    }

    @Override
    public void execute(LoadWorkbookInputData inputData) {
        Path source = inputData.getSource();

        String loadedFileContents;
        try {
            loadedFileContents = dataAccessObject.load(source);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        Workbook workbook;
        try {
            workbook = workbookSerialiser.deserialise(loadedFileContents);
        } catch (ParseException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        LoadWorkbookOutputData outputData = new LoadWorkbookOutputData(workbook);
        presenter.prepareSuccessView(outputData);
    }
}
