package data_access;

import data_access.course_data.CourseDataRepository;
import entity.Section;
import entity.Timetable;
import entity.Workbook;
import entity.CourseOffering;
import use_case.WorkbookDataAccessInterface;
import use_case.add_section.AddSectionDataAccessInterface;

import java.util.List;
import java.util.Optional;

public class AddSectionDataAccessObject implements AddSectionDataAccessInterface {
    private final WorkbookDataAccessInterface workbookDataAccess;
    private final CourseDataRepository courseDataRepository;

    public AddSectionDataAccessObject(CourseDataRepository courseDataRepository,
                                      WorkbookDataAccessInterface workbookDataAccess) {
        this.workbookDataAccess = workbookDataAccess;
        this.courseDataRepository = courseDataRepository;
    }

    // for debugging only
    @Override
    public List<Timetable> getTimetablesFromWorkbook() {
        return workbookDataAccess.getWorkbook().getTimetables();  // Always gets current reference
    }

    @Override
    public Optional<Section> findSection(String courseOfferingIdentifier, String sectionName) {
        CourseOffering courseOfferingToAdd = courseDataRepository.getCourseOffering(courseOfferingIdentifier);
        if (courseOfferingToAdd == null) {
            return Optional.empty();
        }

        for (Section section : courseOfferingToAdd.getAvailableSections()) {
            if (section.getSectionName().equals(sectionName)) {
                return Optional.of(section);
            }
        }
        return Optional.empty();  // Section not found
    }
}