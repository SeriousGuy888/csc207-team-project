package use_case.save_workbook;

import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;

public interface SaveWorkbookDataAccessInterface {
    /**
     * @param workbook    The workbook to save to disk.
     * @param destination The file to write the serialisedWorkbook data to.
     */
    void save(Workbook workbook, Path destination) throws IOException;
}
