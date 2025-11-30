package data_access.course_data;

import entity.CourseOffering;

import java.util.Map;

/**
 * Implementors of this interface handle loading in and storing a library of course offerings that are available,
 * and users of this class may access course offerings from that library.
 */
public interface CourseDataRepositoryGrouped {
    /**
     * @param courseCode The key under which the set of corresponding courses is supposedly stored.
     * @return The corresponding set of course offerings, if it exists. Otherwise, null.
     */
    Map<String, CourseOffering> getMatchingCourseInfo(String courseCode);
}
