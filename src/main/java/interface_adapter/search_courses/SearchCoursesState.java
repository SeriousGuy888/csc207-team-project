package interface_adapter.search_courses;

import java.util.HashSet;
import java.util.Set;

public class SearchCoursesState {
    private final Set<String> matchedCourses;
    private final String errorMessage;

    // No-arg constructor for initial empty state
    public SearchCoursesState() {
        this.matchedCourses = new HashSet<>();
        this.errorMessage = null;
    }

    // Success constructor
    public SearchCoursesState(Set<String> matchedCourses) {
        this.matchedCourses = matchedCourses;
        this.errorMessage = null;
    }

    // Failure constructor
    public SearchCoursesState(String errorMessage) {
        this.matchedCourses = new HashSet<>();
        this.errorMessage = errorMessage;
    }

    public Set<String> getMatchedCourses() {
        return matchedCourses;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return errorMessage != null;
    }
}