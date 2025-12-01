package data_access;

import data_access.workbook_instance.CurrentWorkbook;
import data_access.course_data.CourseDataRepository;
import entity.Section;
import entity.Workbook;
import entity.CourseOffering;
import use_case.add_section.AddSectionDataAccessInterface;
import java.util.Optional;

public class AddSectionDataAccessObject implements AddSectionDataAccessInterface {
    private final CurrentWorkbook currentWorkbook;
    private final CourseDataRepository courseDataRepository;

    public AddSectionDataAccessObject(CourseDataRepository courseDataRepository,
                                      CurrentWorkbook currentWorkbook) {
        this.currentWorkbook = currentWorkbook;
        this.courseDataRepository = courseDataRepository;
    }

    @Override
    public Workbook getWorkbook() {
        return currentWorkbook.getWorkbook();  // Always gets current reference
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