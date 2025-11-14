package data_access.workbook_persistence;

import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface {
    @Override
    public void save(String serialisedWorkbook, Path destination) throws IOException {
        Files.writeString(destination, serialisedWorkbook);
    }
}
