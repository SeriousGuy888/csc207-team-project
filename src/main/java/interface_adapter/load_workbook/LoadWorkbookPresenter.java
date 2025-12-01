package interface_adapter.load_workbook;

import use_case.load_workbook.LoadWorkbookOutputBoundary;
import use_case.load_workbook.LoadWorkbookOutputData;

public class LoadWorkbookPresenter implements LoadWorkbookOutputBoundary {
    @Override
    public void prepareSuccessView(LoadWorkbookOutputData outputData) {
        System.out.println(outputData.getLoadedWorkbook());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println(errorMessage);
    }
}
