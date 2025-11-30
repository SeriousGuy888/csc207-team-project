package use_case.load_workbook;

public interface LoadWorkbookOutputBoundary {
    void prepareSuccessView(LoadWorkbookOutputData outputData);
    void prepareFailView(String errorMessage);
}
