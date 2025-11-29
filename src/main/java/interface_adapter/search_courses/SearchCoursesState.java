package interface_adapter.search_courses;

import java.util.HashSet;
import java.util.Set;

public class SearchCoursesState {
    private final Set<String> matchedCourses;
    private final String errorMessage;

    public SearchCoursesState(Set<String> matchedCourses) {
        this.matchedCourses = matchedCourses;
        this.errorMessage = null;
    }

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