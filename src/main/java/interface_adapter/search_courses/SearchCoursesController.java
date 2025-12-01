package interface_adapter.search_courses;

import use_case.search_courses.SearchCoursesInputBoundary;
import use_case.search_courses.SearchCoursesInputData;

public class SearchCoursesController {
    private final SearchCoursesInputBoundary interactor;

    public SearchCoursesController(SearchCoursesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String query) {
        SearchCoursesInputData inputData = new SearchCoursesInputData(query);
        interactor.execute(inputData);
    }
}