package data_access.autogen;

import entity.CourseOffering;
import use_case.autogen.AutogenDataAccessInterface;
import use_case.autogen.AutogenInputData;

import java.util.List;

public class AutogenCourseDataAccess implements AutogenDataAccessInterface {

    @Override
    public List<CourseOffering> getSelectedCourseOfferings(AutogenInputData inputData) {
        // TODO: replace with real data retrieval logic
        return List.of();
    }
}
