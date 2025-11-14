package interface_adapter.workbook_serialisation;

import entity.Workbook;

public interface WorkbookSerialiser {
    String serialise(Workbook workbook);

    Workbook deserialise(String serialisedWorkbook) throws IllegalArgumentException;
}
