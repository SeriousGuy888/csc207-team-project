package interface_adapter.autogen;

import entity.*;
import use_case.WorkbookDataAccessInterface;
import use_case.autogen.AutogenInputBoundary;
import use_case.autogen.AutogenInputData;
import view.TimetablePanel;
import interface_adapter.TimetableState;

import java.util.HashSet;
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

    public void autogenerate(TimetablePanel timetablePanel) {
        TimetableState timetableState = timetablePanel.getCurrentState();
        if (timetableState == null) {
            // no state yet, nothing to autogen with
            return;
        }


        Workbook workbook = workbookDao.getWorkbook();

        Timetable matched = null;
        for (Timetable t : workbook.getTimetables()) {
            if (t.getTimetableName().equals(timetableState.getTimetableName())) {
                matched = t;
                break;
            }
        }

        if (matched == null) {
            // Fallback: if name mismatch ever happens, just bail or pick first
            return;
        }

        Set<CourseCode> selectedCourses = new HashSet<>();
        for (Section s : matched.getSections()) {
            selectedCourses.add(s.getCourseOffering().getCourseCode());
        }
        Set<Section> lockedSections = new HashSet<>();
        for (TimetableState.SelectedSectionRow row : timetableState.getSelectedSections()) {
            if (!row.isLocked()) {
                continue;
            }

            String rowCourseCode = row.getCourseCode();
            String rowSectionName = row.getSectionName();

            // Find the corresponding Section entity
            for (Section s : matched.getSections()) {
                if (s.getCourseOffering().getCourseCode().toString().equals(rowCourseCode)
                        && s.getSectionName().equals(rowSectionName)) {
                    lockedSections.add(s);
                    break;
                }
            }
        }
        WeeklyOccupancy blockedTimes = null;

        autogenerate(selectedCourses, lockedSections, blockedTimes);
    }
}
