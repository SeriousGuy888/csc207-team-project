package use_case.save_workbook;

import java.io.IOException;
import java.nio.file.Path;

public interface SaveWorkbookDataAccessInterface {
    /**
     * @param serialisedWorkbook The serialisedWorkbook to save to disk.
     * @param destination        The file to write the serialisedWorkbook data to.
     */
    void save(String serialisedWorkbook, Path destination) throws IOException;
}
