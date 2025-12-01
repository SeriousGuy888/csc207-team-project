// use_case/lock_section/LockSectionInteractor.java

package use_case.locksections;

import entity.Section;
import entity.Timetable;
import entity.Workbook;
import entity.CourseCode;
import use_case.WorkbookDataAccessInterface;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputData;

import java.util.Optional;


public class LockSectionInteractor implements LockSectionInputBoundary {

    private final WorkbookDataAccessInterface workbookDao;
    private final TimetableUpdateOutputBoundary presenter;

    public LockSectionInteractor(WorkbookDataAccessInterface workbookDao,
                                 TimetableUpdateOutputBoundary presenter) {
        this.workbookDao = workbookDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(LockSectionInputData inputData) {
        Workbook workbook = workbookDao.getWorkbook();
        int tabIndex = inputData.getTabIndex();

        if (tabIndex < 0 || tabIndex >= workbook.getTimetables().size()) {
            return; // invalid tab, nothing to do
        }

        Timetable timetable = workbook.getTimetables().get(tabIndex);

        CourseCode code = new CourseCode(inputData.getCourseCode());

        Optional<Section> maybeSection = timetable.getSections().stream()
                .filter(s -> s.getCourseOffering().getCourseCode().equals(code)
                        && s.getSectionName().equals(inputData.getSectionName()))
                .findFirst();

        if (maybeSection.isEmpty()) {
            return; // no matching section in this timetable
        }

        Section section = maybeSection.get();

        if (inputData.isLocked()) {
            timetable.lockSection(section);
        } else {
            timetable.unlockSection(section);
        }

        workbookDao.saveWorkbook(workbook);

        TimetableUpdateOutputData out = new TimetableUpdateOutputData(timetable, tabIndex);
        presenter.prepareSuccessView(out);
    }
}
