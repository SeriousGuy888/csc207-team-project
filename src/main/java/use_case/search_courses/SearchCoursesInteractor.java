package use_case.search_courses;

import java.io.IOException;
import java.util.Set;

import entity.CourseOffering;

public class SearchCoursesInteractor implements SearchCoursesInputBoundary {
    private final SearchCoursesDataAccessInterface dataAccessObject;
    private final SearchCoursesOutputBoundary presenter;

    public SearchCoursesInteractor(SearchCoursesDataAccessInterface dataAccessObject,
                                    SearchCoursesOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchCoursesInputData inputData) {
        String query = inputData.getQuery();

        Set<CourseOffering> matchedCourses;
        try {
            matchedCourses = dataAccessObject.searchCourses(query);
        } catch (IOException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        SearchCoursesOutputData outputData = new SearchCoursesOutputData(matchedCourses);
        presenter.prepareSuccessView(outputData);
    }
}