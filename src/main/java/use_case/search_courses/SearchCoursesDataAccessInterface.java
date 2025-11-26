package use_case.search_courses;

public interface SearchCoursesDataAccessInterface {
    List<CourseOffering> searchCourses(String query);
}