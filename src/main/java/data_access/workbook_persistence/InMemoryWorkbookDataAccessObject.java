package data_access.workbook_persistence;

import entity.Workbook;
import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A class used for <strong>testing</strong> workbook persistence operations.
 * Data saved by this class isn't actually persistent; it's stored temporarily in memory.
 */
public class InMemoryWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface {
    private final Map<Path, Workbook> fakeFilesystem = new HashMap<>();

    @Override
    public void save(Workbook workbook, Path destination) {
        fakeFilesystem.put(destination, workbook);
    }
}
