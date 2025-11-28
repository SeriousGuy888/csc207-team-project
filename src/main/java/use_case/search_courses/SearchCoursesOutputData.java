package use_case.search_courses;

import java.util.Set;

import entity.CourseOffering;

public class SearchCoursesOutputData {
    private final Set<CourseOffering> matchedCourses;

    public SearchCoursesOutputData(Set<CourseOffering> matchedCourses) {
        this.matchedCourses = matchedCourses;
    }

    public Set<CourseOffering> getMatchedCourses() {
        return matchedCourses;
    }
}