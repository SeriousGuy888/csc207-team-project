package use_case.load_workbook;

import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;

public class LoadWorkbookInteractor implements LoadWorkbookInputBoundary {
    private final LoadWorkbookDataAccessInterface dataAccessObject;
    private final LoadWorkbookOutputBoundary presenter;


    public LoadWorkbookInteractor(LoadWorkbookDataAccessInterface dataAccessObject,
                                  LoadWorkbookOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadWorkbookInputData inputData) {
        Path source = inputData.getSource();

        Workbook workbook;
        try {
            workbook = dataAccessObject.load(source);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        LoadWorkbookOutputData outputData = new LoadWorkbookOutputData(workbook);
        presenter.prepareSuccessView(outputData);
    }
}
