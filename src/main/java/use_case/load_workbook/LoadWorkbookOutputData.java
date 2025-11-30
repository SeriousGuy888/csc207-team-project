package use_case.load_workbook;

import entity.Workbook;

public class LoadWorkbookOutputData {
    private final Workbook loadedWorkbook;

    public LoadWorkbookOutputData(Workbook loadedWorkbook) {
        this.loadedWorkbook = loadedWorkbook;
    }

    public Workbook getLoadedWorkbook() {
        return loadedWorkbook;
    }
}
