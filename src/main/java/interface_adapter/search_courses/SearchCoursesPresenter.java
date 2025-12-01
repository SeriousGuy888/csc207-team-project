package interface_adapter.search_courses;

import java.util.HashSet;
import java.util.Set;

import entity.CourseOffering;

import use_case.search_courses.SearchCoursesOutputBoundary;
import use_case.search_courses.SearchCoursesOutputData;

public class SearchCoursesPresenter implements SearchCoursesOutputBoundary {
    private final SearchCoursesViewModel viewModel;

    public SearchCoursesPresenter(SearchCoursesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private Set<String> getCourseOfferingStrings(SearchCoursesOutputData outputData) {
        Set<String> courseofferingstrings = new HashSet<>();
        for (CourseOffering courseoffering : outputData.getMatchedCourses()){
            courseofferingstrings.add(getDisplayString(courseoffering));
        }
        return courseofferingstrings;
    }
    
    private String getDisplayString(CourseOffering courseoffering) {
        String courseID = courseoffering.getUniqueIdentifier();
        // hardcoded for now
        if (courseID.endsWith("20259")) {
            return courseoffering.getCourseCode().toString() + "-F";
        }
        return courseoffering.getCourseCode().toString() + "-W";
    }

    @Override
    public void prepareSuccessView(SearchCoursesOutputData outputData) {
        Set<String> courseofferingstrings = getCourseOfferingStrings(outputData);

        viewModel.setState(new SearchCoursesState(courseofferingstrings));

        viewModel.firePropertyChange(SearchCoursesViewModel.SEARCH_RESULTS_UPDATED);

    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setState(new SearchCoursesState(errorMessage));

        viewModel.firePropertyChange(SearchCoursesViewModel.SEARCH_RESULTS_UPDATED);

    }
}