package data_access.workbook_persistence.strategies;

import entity.Workbook;

import java.text.ParseException;

public abstract class WorkbookSerialiser {
    public abstract String serialise(Workbook workbook);
    public abstract Workbook deserialise(String serialisedWorkbook) throws ParseException;
}
