package use_case.load_workbook;

import entity.Workbook;

public class LoadWorkbookOutputData {
    private final String message;

    public LoadWorkbookOutputData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
