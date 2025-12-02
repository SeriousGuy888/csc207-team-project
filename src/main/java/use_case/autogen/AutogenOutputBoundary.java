package use_case.autogen;

public interface AutogenOutputBoundary {
    void prepareSuccessView(AutogenOutputData outputData);
    void prepareFailView(String errorMessage);
}