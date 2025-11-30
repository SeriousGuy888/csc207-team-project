package interface_adapter.save_workbook;

import use_case.save_workbook.SaveWorkbookOutputBoundary;
import use_case.save_workbook.SaveWorkbookOutputData;

public class SaveWorkbookPresenter implements SaveWorkbookOutputBoundary {
    @Override
    public void prepareSuccessView(SaveWorkbookOutputData outputData) {
        System.out.println(outputData.getDestination());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("error: " + errorMessage);
    }
}
