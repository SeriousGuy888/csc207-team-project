package use_case.save_workbook;

public interface SaveWorkbookOutputBoundary {
    void prepareSuccessView(SaveWorkbookOutputData outputData);
    void prepareFailView(String errorMessage);
}
