package data_access.workbook_persistence.strategies;

import entity.Workbook;

public abstract class WorkbookSerialiser {
    public abstract String serialise(Workbook workbook);
    public abstract Workbook deserialise(String serialisedWorkbook) throws IllegalArgumentException;
}
