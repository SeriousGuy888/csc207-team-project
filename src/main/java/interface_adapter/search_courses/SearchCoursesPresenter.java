//package interface_adapter.search_courses;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import entity.CourseOffering;
//
//import use_case.search_courses.SearchCoursesOutputBoundary;
//import use_case.search_courses.SearchCoursesOutputData;
//
//public class SearchCoursesPresenter implements SearchCoursesOutputBoundary {
//    private final SearchCoursesViewModel viewModel;
//
//    public SearchCoursesPresenter(SearchCoursesViewModel viewModel) {
//        this.viewModel = viewModel;
//    }
//
//    private Set<String> getCourseOfferingStrings(SearchCoursesOutputData outputData) {
//        Set<String> courseofferingstrings = new HashSet<>();
//        for (CourseOffering courseoffering : outputData.getMatchedCourses()){
//            courseofferingstrings.add(courseoffering.toString());
//        }
//        return courseofferingstrings;
//    }
//
//    @Override
//    public void prepareSuccessView(SearchCoursesOutputData outputData) {
//        Set<String> courseofferingstrings = getCourseOfferingStrings(outputData);
//
//        viewModel.setState(new SearchCoursesState(courseofferingstrings));
//
//        viewModel.fireStateChangeEvent();
//    }
//
//    @Override
//    public void prepareFailView(String errorMessage) {
//        viewModel.setState(new SearchCoursesState(errorMessage));
//
//        viewModel.fireStateChangeEvent();
//    }
//}