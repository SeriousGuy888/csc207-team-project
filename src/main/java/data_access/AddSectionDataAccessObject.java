package data_access;

import data_access.workbook_instance.CurrentWorkbook;
import data_access.course_data.CourseDataRepository;
import entity.Section;
import entity.Workbook;
import entity.CourseOffering;
import use_case.addsection.AddSectionDataAccessInterface;

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
    public Section getSectionToAdd(String courseOfferingIdentifier, String sectionName) {
        CourseOffering courseOfferingToAdd = courseDataRepository.getCourseOffering(courseOfferingIdentifier);

        for (Section section : courseOfferingToAdd.getAvailableSections()) {
            if (section.getName().equals(sectionName)) {
                return section;
            }
        }
        return null;  // Section not found
    }
}