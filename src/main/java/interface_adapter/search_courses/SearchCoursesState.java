package interface_adapter.search_courses;

import java.util.HashSet;
import java.util.Set;

public class SearchCoursesState {
    private final Set<String> courseOfferingStrings;
    private final String errorMessage;

    public SearchCoursesState() {
        this.courseOfferingStrings = new HashSet<>();
        this.errorMessage = null;
    }

    public SearchCoursesState(Set<String> courseOfferingStrings) {
        this.courseOfferingStrings = courseOfferingStrings;
        this.errorMessage = null;
    }

    public SearchCoursesState(String errorMessage) {
        this.courseOfferingStrings = new HashSet<>();
        this.errorMessage = errorMessage;
    }

    public Set<String> getCourseOfferingStrings() {
        return courseOfferingStrings;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}