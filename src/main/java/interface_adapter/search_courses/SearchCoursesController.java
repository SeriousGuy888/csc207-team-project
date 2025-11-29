package interface_adapter.search_courses;

import use_case.search_courses.SearchCoursesInputBoundary;
import use_case.search_courses.SearchCoursesInputData;

public class SearchCoursesController {

    final SearchCoursesInputBoundary searchCoursesUseCaseInteractor;

    public SearchCoursesController(SearchCoursesInputBoundary searchCoursesUseCaseInteractor) {
        this.searchCoursesUseCaseInteractor = searchCoursesUseCaseInteractor;
    }

    public void execute(String query) {
        SearchCoursesInputData inputData = new SearchCoursesInputData(query);
        searchCoursesUseCaseInteractor.execute(inputData);
    }
}