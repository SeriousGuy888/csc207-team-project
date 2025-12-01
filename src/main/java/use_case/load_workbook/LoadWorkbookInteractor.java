package use_case.load_workbook;

import data_access.WorkbookDataAccessObject;
import data_access.course_data.CourseDataRepository;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;

public class LoadWorkbookInteractor implements LoadWorkbookInputBoundary {
    private final WorkbookDataAccessInterface currentWorkbookDao;
    private final LoadWorkbookDataAccessInterface loadingDao;
    private final LoadWorkbookOutputBoundary presenter;


    public LoadWorkbookInteractor(WorkbookDataAccessInterface currentWorkbookDao,
                                  LoadWorkbookDataAccessInterface loadingDao,
                                  LoadWorkbookOutputBoundary presenter) {
        this.currentWorkbookDao = currentWorkbookDao;
        this.loadingDao = loadingDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadWorkbookInputData inputData) {
        final Path source = inputData.getSource();

        final Workbook workbook;
        try {
            workbook = loadingDao.load(source);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        currentWorkbookDao.saveWorkbook(workbook);

        final LoadWorkbookOutputData outputData = new LoadWorkbookOutputData(
                "Successfully loaded workbook containing " + workbook.getTimetables().size() + " timetables"
                        + " from " + source.toAbsolutePath() + "."
        );
        presenter.prepareSuccessView(outputData);
    }
}
