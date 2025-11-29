package use_case.addsection;

import entity.Section;
import entity.Timetable;
import entity.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class AddSectionInteractor implements AddSectionInputBoundary {

    private final AddSectionDataAccessInterface dataAccess;
    private final AddSectionOutputBoundary presenter;

    // TODO: Get actual session codes from app state or user selection
    // For now, try both Fall 2025 and Winter 2026
    private static final String[] IDENTIFIER_SUFFIXES = {"-20259", "-20261"};

    public AddSectionInteractor(AddSectionDataAccessInterface dataAccess,
                                 AddSectionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddSectionInputData inputData) {
        String courseCode = inputData.getCourseCode();
        String sectionName = inputData.getSectionName();
        int timetableIndex = inputData.getTimetableIndex();

        // 1. Get workbook and validate timetable index
        Workbook workbook = dataAccess.getWorkbook();
        List<Timetable> timetables = workbook.getTimetables();
        Timetable timetable = timetables.get(timetableIndex);

        // 2. Find the Section entity
        // TODO: full courseOfferingId
        Optional<Section> sectionOpt = findSectionWithFallback(courseCode, sectionName);

        if (sectionOpt.isEmpty()) {
            presenter.prepareFailView("Section not found: " + courseCode + " " + sectionName);
            return;
        }

        Section sectionToAdd = sectionOpt.get();

        // 3. Check if already in timetable
        if (timetable.getSections().contains(sectionToAdd)) {
            presenter.prepareFailView("Section already in timetable: " + courseCode + " " + sectionName);
            return;
        }

        // 4. Check for time conflicts
        List<String> conflicts = findConflicts(sectionToAdd, timetable);

        // 5. Add the section
        boolean added = timetable.addSection(sectionToAdd);

        if (!added) {
            presenter.prepareFailView("Failed to add section: " + courseCode + " " + sectionName);
            return;
        }

        // 6. Prepare output
        AddSectionOutputData outputData = new AddSectionOutputData(
                timetable,
                timetableIndex,
                courseCode,
                sectionName,
                conflicts
        );

        if (conflicts.isEmpty()) {
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareConflictView(outputData);
        }
    }

    /**
     * Tries to find section by attempting different session suffixes.
     * TODO: Remove this workaround once UI provides full courseOfferingId
     */
    private Optional<Section> findSectionWithFallback(String courseCode, String sectionName) {
        for (String suffix : SESSION_SUFFIXES) {
            String courseOfferingId = courseCode + suffix;
            Optional<Section> section = dataAccess.findSection(courseOfferingId, sectionName);
            if (section.isPresent()) {
                return section;
            }
        }
        return Optional.empty();
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