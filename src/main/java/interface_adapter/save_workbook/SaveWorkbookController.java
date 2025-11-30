package interface_adapter.save_workbook;

import use_case.save_workbook.SaveWorkbookInputData;
import use_case.save_workbook.SaveWorkbookInteractor;

import java.nio.file.Path;

public class SaveWorkbookController {
    private final SaveWorkbookInteractor interactor;

    public SaveWorkbookController(SaveWorkbookInteractor interactor) {
        this.interactor = interactor;
    }

    /**
     * Attempt to save the currently open workbook to disk.
     * @param path the path to save the currently open workbook to
     */
    public void execute(Path path) {
        // todo: fix enttieis being expected on frontend
        final SaveWorkbookInputData inputData = new SaveWorkbookInputData(null, path);
        interactor.execute(inputData);
    }
}
