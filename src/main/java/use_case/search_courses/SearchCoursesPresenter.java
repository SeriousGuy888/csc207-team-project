package use_case.search_courses;

public class SearchCoursesPresenter implements SearchCoursesOutputBoundary {
    private final SearchCoursesViewInterface view;

    public SearchCoursesPresenter(SearchCoursesViewInterface view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(SearchCoursesOutputData outputData) {
        view.displayCourses(outputData.getMatchedCourses());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        view.displayError(errorMessage);
    }
}