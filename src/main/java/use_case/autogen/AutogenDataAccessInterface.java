package use_case.autogen;

import entity.CourseOffering;
import  java.util.List;


public interface AutogenDataAccessInterface {
    List<CourseOffering> getSelectedCourseOfferings(AutogenInputData inputData);
}
