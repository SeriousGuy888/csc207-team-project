package data_access.course_data;

import entity.CourseOffering;

/**
 * Implementors of this interface handle loading in and storing a library of course offerings that are available,
 * and users of this class may access course offerings from that library.
 */
public interface CourseDataRepository {
    /**
     * @param courseOfferingIdentifier The key under which the course you want is supposedly stored.
     * @return The corresponding course offering, if it exists. Otherwise, null.
     */
    CourseOffering getCourseOffering(String courseOfferingIdentifier);
    Set<String> getAllCourseOfferingIdentifiers();
}
