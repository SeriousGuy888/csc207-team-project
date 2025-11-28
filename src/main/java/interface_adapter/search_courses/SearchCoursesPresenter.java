package interface_adapter.search_courses;

import entity.CourseOffering;

import use_case.search_courses.SearchCoursesOutputBoundary;
import use_case.search_courses.SearchCoursesOutputData;

public class SearchCoursesPresenter implements SearchCoursesOutputBoundary {

    private String getRawCourseString(CourseOffering courseoffering) {
         return courseoffering.toString();
    }

    @Override
    public void prepareSuccessView(SearchCoursesOutputData outputData) {
        for (CourseOffering courseoffering : outputData.getMatchedCourses()){
            String CourseOfferingString = getRawCourseString(courseoffering);
            System.out.println(CourseOfferingString);
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println(errorMessage);
    }
}