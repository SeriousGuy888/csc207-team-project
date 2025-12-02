package use_case.load_workbook;

import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;

public class LoadWorkbookInteractor implements LoadWorkbookInputBoundary {
    private final WorkbookDataAccessInterface currentWorkbookDao;
    private final LoadWorkbookDataAccessInterface loadingDao;
    private final LoadWorkbookOutputBoundary dialogPresenter;
    private final LoadWorkbookGlobalStateOutputBoundary globalViewPresenter;


    public LoadWorkbookInteractor(WorkbookDataAccessInterface currentWorkbookDao,
                                  LoadWorkbookDataAccessInterface loadingDao,
                                  LoadWorkbookOutputBoundary dialogPresenter,
                                  LoadWorkbookGlobalStateOutputBoundary globalViewPresenter) {
        this.currentWorkbookDao = currentWorkbookDao;
        this.loadingDao = loadingDao;
        this.dialogPresenter = dialogPresenter;
        this.globalViewPresenter = globalViewPresenter;
    }

    @Override
    public void execute(LoadWorkbookInputData inputData) {
        final Path source = inputData.getSource();

        final Workbook workbook;
        try {
            workbook = loadingDao.load(source);
        } catch (IOException e) {
            dialogPresenter.prepareFailView(e.getMessage());
            return;
        }

        currentWorkbookDao.saveWorkbook(workbook);
        globalViewPresenter.prepareSuccessView(workbook);

        final LoadWorkbookOutputData outputData = new LoadWorkbookOutputData(
                "Successfully loaded workbook containing " + workbook.getTimetables().size() + " timetables"
                        + " from " + source.toAbsolutePath() + "."
        );
        dialogPresenter.prepareSuccessView(outputData);
    }
}
