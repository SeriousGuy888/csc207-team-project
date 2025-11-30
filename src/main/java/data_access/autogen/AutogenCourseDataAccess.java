package data_access.autogen;

import entity.CourseCode;
import entity.CourseOffering;
import use_case.autogen.AutogenDataAccessInterface;
import use_case.autogen.AutogenInputData;

import java.util.List;
import java.util.Set;

public class AutogenCourseDataAccess implements AutogenDataAccessInterface {

    @Override
    public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
        // TODO: replace with real data retrieval logic
        return List.of();
    }
}
