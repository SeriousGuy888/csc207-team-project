package use_case.search_courses;

public interface SearchCoursesDataAccessInterface {
    Set<CourseOffering> searchCourses(String query);
}