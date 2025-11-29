package use_case.add_section;

import entity.Section;
import entity.Workbook;

import java.util.Optional;

public interface AddSectionDataAccessInterface {

    // Get the current workbook containing all timetables.
    Workbook getWorkbook();

    // Finds the corresponding Section instance to add.
    // TODO: see if we can change parameter + DAO to courseOfferingAsString
    // Returns an Optional to indicate when the section isn't found.
    Optional<Section> findSection(String courseOfferingId, String sectionName);
}