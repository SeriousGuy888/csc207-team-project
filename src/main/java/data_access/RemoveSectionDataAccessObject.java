package data_access;

import data_access.course_data.CourseDataRepository;
import entity.Section;
import entity.Timetable;
import entity.CourseOffering;
import use_case.WorkbookDataAccessInterface;
import use_case.remove_section.RemoveSectionDataAccessInterface;

import java.util.List;
import java.util.Optional;

public class RemoveSectionDataAccessObject implements RemoveSectionDataAccessInterface {
    private final WorkbookDataAccessInterface workbookDataAccess;
    private final CourseDataRepository courseDataRepository;

    public RemoveSectionDataAccessObject(CourseDataRepository courseDataRepository,
                                         WorkbookDataAccessInterface workbookDataAccess) {
        this.workbookDataAccess = workbookDataAccess;
        this.courseDataRepository = courseDataRepository;
    }

    @Override
    public List<Timetable> getTimetablesFromWorkbook() {
        return workbookDataAccess.getWorkbook().getTimetables();
    }

    @Override
    public Optional<Section> findSection(String courseOfferingIdentifier, String sectionName) {
        CourseOffering courseOfferingToRemove = courseDataRepository.getCourseOffering(courseOfferingIdentifier);
        if (courseOfferingToRemove == null) {
            return Optional.empty();
        }

        for (Section section : courseOfferingToRemove.getAvailableSections()) {
            if (section.getSectionName().equals(sectionName)) {
                return Optional.of(section);
            }
        }
        return Optional.empty();
    }
}