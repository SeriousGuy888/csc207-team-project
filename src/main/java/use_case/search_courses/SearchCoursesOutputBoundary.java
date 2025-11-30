package use_case.search_courses;

public interface SearchCoursesOutputBoundary {
    void prepareSuccessView(SearchCoursesOutputData outputData);
    void prepareFailView(String errorMessage);
}
