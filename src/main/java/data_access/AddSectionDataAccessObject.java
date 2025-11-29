package data_access;

import entity.Section;
import entity.Workbook;
import entity.CourseOffering;

import use_case.addsection.AddSectionDataAccessInterface;
import course_data.CourseDataRepository;

/**
 * Class to retrieve Section and Workbook data for the Add Section use case.
 * Relies on two shared DAOs: CourseDataRepository and the Workbook instance.
 * Assumes both are properly initialized at application start-up and will not change during runtime.
 */
public class AddSectionDataAccessObject implements AddSectionDataAccessInterface {
    private final Workbook workbook;
    private final CourseDataRepository courseDataRepository;

    public AddSectionDataAccessObject(CourseDataRepository courseDataRepository,
                                      Workbook workbook) {
        this.workbook = workbook;
        this.courseDataRepository = courseDataRepository;
    }

    @Override
    public Workbook getWorkbook() {
        return workbook;
    }


    @Override
    public Section getSectionToAdd(String courseOfferingIdentifier, String sectionName) throws IllegalArgumentException {
        CourseOffering courseOfferingToAdd = courseDataRepository.getCourseOffering(courseOfferingIdentifier);

        // sectionName is taken from our UI so should be in expected format
        for (Section section : courseOfferingToAdd.getAvailableSections()) {
            if (section.getName().equals(sectionName)) {
                return section;
            }
        }
        throw new IllegalArgumentException("Section " + sectionName + " not found in Course Offering "
                + courseOfferingId);
    }
}