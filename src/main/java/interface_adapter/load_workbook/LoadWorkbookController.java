package interface_adapter.load_workbook;

import use_case.load_workbook.LoadWorkbookInputData;
import use_case.load_workbook.LoadWorkbookInteractor;

import java.nio.file.Path;

public class LoadWorkbookController {
    private final LoadWorkbookInteractor interactor;

    public LoadWorkbookController(LoadWorkbookInteractor interactor) {
        this.interactor = interactor;
    }

    /**
     * Attempt to load a workbook in from disk and replace the currently open workbook with that loaded file.
     * @param path the path to try to load from
     */
    public void execute(Path path) {
        final LoadWorkbookInputData inputData = new LoadWorkbookInputData(path);
        interactor.execute(inputData);
    }
}
