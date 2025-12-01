package use_case.add_section;

import entity.Section;
import entity.Timetable;
import entity.Workbook;
import use_case.timetable_update.TimetableUpdateOutputBoundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class AddSectionInteractor implements AddSectionInputBoundary {
    private final AddSectionDataAccessInterface dataAccess;
    // No separate presenter, it will be an update to the timetable in the main panel
    private final TimetableUpdateOutputBoundary presenter;

    public AddSectionInteractor(AddSectionDataAccessInterface dataAccess,
                                 TimetableUpdateOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    // also hardcoded we present tmrw sorry guys
    private String getCourseIdentifier(String courseDisplayString) {
        if (courseDisplayString.endsWith('-F')) {
            return courseDisplayString.substring(0, 9) + "20259";
        }
        return courseDisplayString.substring(0, 9) + "20261";
    }

    @Override
    public void execute(AddSectionInputData inputData) {
        String courseOfferingIdentifier = getCourseIdentifier(inputData.getCourseDisplayString());
        String sectionName = inputData.getSectionName();
        int selectedTabIndex = inputData.getSelectedTabIndex();

        List<Timetable> timetables  = dataAccess.getTimetablesFromWorkbook();
        Timetable timetable = timetables.get(selectedTabIndex);

        Optional<Section> sectionOpt = dataAccess.findSection(courseOfferingIdentifier, sectionName);

        if (sectionOpt.isEmpty()) {
            presenter.prepareFailView("Section not found: " + courseOfferingAsString + " " + sectionName);
            return;
        }

        Section sectionToAdd = sectionOpt.get();

        // Check if already in timetable
        if (timetable.getSections().contains(sectionToAdd)) {
            presenter.prepareFailView("Section already in timetable: " + courseOfferingAsString + " " + sectionName);
            return;
        }

        // Check for time conflicts
        List<String> conflicts = findConflicts(sectionToAdd, timetable);

        // Add the section
        boolean added = timetable.addSection(sectionToAdd);

        if (!added) {
            presenter.prepareFailView("Failed to add section: " + courseOfferingAsString + " " + sectionName);
            return;
        }

        // Prepare output
        AddSectionOutputData outputData = new AddSectionOutputData(
            timetable,
            selectedTabIndex,
            courseOfferingAsString,
            sectionName,
            conflicts
        );
        presenter.prepareSuccessView(outputData);
    }

    private List<String> findConflicts(Section sectionToAdd, Timetable timetable) {
        List<String> conflicts = new ArrayList<>();

        for (Section existing : timetable.getSections()) {
            if (sectionToAdd.getOccupiedTime().doesIntersect(existing.getOccupiedTime())) {
                String conflictName = existing.getCourseOffering().getCourseCode().toString()
                        + " " + existing.getSectionName();
                conflicts.add(conflictName);
            }
        }

        return conflicts;
    }
}