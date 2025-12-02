package use_case.add_section;


public interface AddSectionOutputBoundary {

    void prepareSuccessView(AddSectionOutputData outputData);
    void prepareFailView(String errorMessage);
    
}