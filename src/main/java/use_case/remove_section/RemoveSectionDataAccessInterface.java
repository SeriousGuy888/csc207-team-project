package use_case.remove_section;

import entity.Section;
import entity.Timetable;
import entity.Workbook;

import java.util.List;
import java.util.Optional;

public interface RemoveSectionDataAccessInterface {

    // Get the current workbook containing all timetables.
    List<Timetable> getTimetablesFromWorkbook();

    // Finds the corresponding Section instance to add.
    // TODO: see if we can change parameter + DAO to courseOfferingAsString
    // Returns an Optional to indicate when the section isn't found.
    Optional<Section> findSection(String courseOfferingId, String sectionName);
}