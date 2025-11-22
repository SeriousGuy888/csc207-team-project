package interface_adapter.autogen;

import entity.CourseCode;
import use_case.autogen.AutogenInputBoundary;
import use_case.autogen.AutogenInputData;
import entity.Section;
import entity.WeeklyOccupancy;

import java.util.Set;

public class AutogenController {
    private final AutogenInputBoundary interactor;

    public AutogenController(AutogenInputBoundary interactor){this.interactor=interactor;}

    public void generate(Set<CourseCode> selectedCourses,
                         Set<Section> lockedSections,
                         WeeklyOccupancy blockedTimes){
        AutogenInputData data = new AutogenInputData(selectedCourses,lockedSections,blockedTimes);
        interactor.execute(data);
    }
}
