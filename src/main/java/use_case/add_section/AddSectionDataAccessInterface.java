package use_case.addsection;

import entity.Section;
import entity.Workbook;

import java.util.Optional;

public interface AddSectionDataAccessInterface {

    // Get the current workbook containing all timetables.
    Workbook getWorkbook();

    // Finds the corresponding Section instance to add.
    // TODO: see if we can change parameter + DAO to courseOfferingAsString
    Section getSectionToAdd(String courseOfferingId, String sectionName);
}