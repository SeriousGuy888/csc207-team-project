package data_access.workbook_persistence;

import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A class used for <strong>testing</strong> workbook persistence operations.
 * Data saved by this class isn't actually persistent; it's stored temporarily in memory.
 */
public class InMemoryWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface {
    private final Map<Path, String> fakeFilesystem = new HashMap<>();

    @Override
    public void save(String serialisedWorkbook, Path destination) {
        fakeFilesystem.put(destination, serialisedWorkbook);
    }

    public String load(Path source) throws IOException {
        if(!fakeFilesystem.containsKey(source)) {
            throw new IOException("Saved data not found at this path.");
        }

        return fakeFilesystem.get(source);
    }
}
