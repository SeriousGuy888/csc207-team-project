package data_access.workbook_persistence;

import entity.Workbook;
import use_case.load_workbook.LoadWorkbookDataAccessInterface;
import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface, LoadWorkbookDataAccessInterface {
    @Override
    public void save(String serialisedWorkbook, Path destination) throws IOException {
        Files.createDirectories(destination.subpath(0, destination.getNameCount() - 1));
        Files.writeString(destination, serialisedWorkbook);
    }

    @Override
    public String load(Path source) throws IOException {
        return Files.readString(source);
    }
}
