package interface_adapter.autogen;

import entity.CourseCode;
import entity.Section;
import entity.WeeklyOccupancy;
import use_case.WorkbookDataAccessInterface;
import use_case.autogen.AutogenInputBoundary;
import use_case.autogen.AutogenInputData;

import java.util.Set;

public class AutogenController {

    private final AutogenInputBoundary interactor;
    private final WorkbookDataAccessInterface workbookDao;

    public AutogenController(AutogenInputBoundary interactor,
                             WorkbookDataAccessInterface workbookDao) {
        this.interactor = interactor;
        this.workbookDao = workbookDao;
    }

    /**
     * Full version: caller passes in the actual user choices.
     */
    public void autogenerate(Set<CourseCode> selectedCourses,
                             Set<Section> lockedSections,
                             WeeklyOccupancy blockedTimes) {

        AutogenInputData input = new AutogenInputData(
                selectedCourses,
                lockedSections,
                blockedTimes
        );

        interactor.execute(input);
    }


    public void autogenerate() {
        // TODO later:
        //  - get selectedCourses from workbook or SearchPanel selections
        //  - get lockedSections from the lock table
        //  - get blockedTimes from a "blocked time" UI
        autogenerate(Set.of(), Set.of(), null);
    }
}
