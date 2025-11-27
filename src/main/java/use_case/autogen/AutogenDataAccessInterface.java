package use_case.autogen;
import entity.CourseCode;
import entity.CourseOffering;
import java.util.List;
import java.util.Set;


public interface AutogenDataAccessInterface {
    List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses);
}
