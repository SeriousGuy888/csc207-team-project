package interface_adapter;

import entity.Workbook;

public class GlobalViewState {
    private Workbook loadedWorkbook;

    public Workbook getLoadedWorkbook() {
        return loadedWorkbook;
    }

    public void setLoadedWorkbook(Workbook loadedWorkbook) {
        this.loadedWorkbook = loadedWorkbook;
    }
}
