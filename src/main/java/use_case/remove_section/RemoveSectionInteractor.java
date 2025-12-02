package use_case.remove_section;

import entity.Section;
import entity.Timetable;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputData;

import java.util.List;
import java.util.Optional;

public class RemoveSectionInteractor implements RemoveSectionInputBoundary {
    private final RemoveSectionDataAccessInterface dataAccess;
    private final TimetableUpdateOutputBoundary presenter;

    public RemoveSectionInteractor(RemoveSectionDataAccessInterface dataAccess,
                                   TimetableUpdateOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    // Hardcoded session mapping (same as AddSectionInteractor)
    private String getCourseIdentifier(String courseDisplayString) {
        if (courseDisplayString.endsWith("F") || courseDisplayString.endsWith("Y")) {
            return courseDisplayString + "-20259";
        }
        return courseDisplayString + "-20261";
    }

    @Override
    public void execute(RemoveSectionInputData inputData) {
        String courseOfferingIdentifier = getCourseIdentifier(inputData.getCourseDisplayString());
        String sectionName = inputData.getSectionName();
        int selectedTabIndex = inputData.getSelectedTabIndex();

        List<Timetable> timetables = dataAccess.getTimetablesFromWorkbook();
        Timetable timetable = timetables.get(selectedTabIndex);

        Optional<Section> sectionOpt = dataAccess.findSection(courseOfferingIdentifier, sectionName);

        if (sectionOpt.isEmpty()) {
            presenter.prepareFailView("Section not found: " + courseOfferingIdentifier + " " + sectionName);
            return;
        }

        Section sectionToRemove = sectionOpt.get();

        // Check if section is actually in the timetable
        if (!timetable.getSections().contains(sectionToRemove)) {
            presenter.prepareFailView("Section not in timetable: " + courseOfferingIdentifier + " " + sectionName);
            return;
        }

        // Remove the section
        boolean removed = timetable.removeSection(sectionToRemove);

        if (!removed) {
            presenter.prepareFailView("Failed to remove section: " + courseOfferingIdentifier + " " + sectionName);
            return;
        }

        // Prepare timetable output
        TimetableUpdateOutputData outputData = new TimetableUpdateOutputData(
                timetable,
                selectedTabIndex
        );
        presenter.prepareSuccessView(outputData);
    }
}