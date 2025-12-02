package interface_adapter.save_workbook;

import use_case.save_workbook.SaveWorkbookInputBoundary;
import use_case.save_workbook.SaveWorkbookInputData;

import java.nio.file.Path;

public class SaveWorkbookController {
    private final SaveWorkbookInputBoundary interactor;

    public SaveWorkbookController(SaveWorkbookInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Attempt to save the currently open workbook to disk.
     *
     * @param path the path to save the currently open workbook to
     */
    public void execute(Path path) {
        final SaveWorkbookInputData inputData = new SaveWorkbookInputData(path);
        interactor.execute(inputData);
    }
}
