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
            // could call presenter.prepareFailView, but for now just ignore
            return;
        }

        Timetable timetable = workbook.getTimetables().get(tabIndex);

        // Find the corresponding Section in this timetable
        CourseCode code = new CourseCode(inputData.getCourseCode());

        Optional<Section> maybeSection = timetable.getSections().stream()
                .filter(s -> s.getCourseOffering().getCourseCode().equals(code)
                        && s.getSectionName().equals(inputData.getSectionName()))
                .findFirst();

        if (maybeSection.isEmpty()) {
            // Section not found in this timetable; bail out
            return;
        }

        Section section = maybeSection.get();

        if (inputData.isLocked()) {
            timetable.lockSection(section);
        } else {
            timetable.unlockSection(section);
        }

        // Persist changes
        workbookDao.saveWorkbook(workbook);

        // Notify the view that this timetable has updated (lock status affects UI)
        TimetableUpdateOutputData out =
                new TimetableUpdateOutputData(timetable, tabIndex);
        presenter.prepareSuccessView(out);
    }
}
