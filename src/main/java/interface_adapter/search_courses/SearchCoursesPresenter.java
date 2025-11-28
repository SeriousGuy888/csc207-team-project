package interface_adapter.search_courses;

import use_case.search_courses.SearchCoursesOutputBoundary;
import use_case.search_courses.SearchCoursesOutputData;

public class SearchCoursesPresenter implements SearchCoursesOutputBoundary {
    @Override
    public void prepareSuccessView(SearchCoursesOutputData outputData) {
        System.out.println(outputData.getMatchedCourses());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println(errorMessage);
    }
}