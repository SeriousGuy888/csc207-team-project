package use_case.osrm_walktime;

public interface WalkTimeOutputBoundary {
    void prepareSuccessView(WalkTimeOutputData outputData);
    void prepareFailView(String error);
}