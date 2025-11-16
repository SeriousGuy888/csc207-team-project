package use_case.load_workbook;

import entity.Workbook;

import java.io.IOException;
import java.nio.file.Path;

public interface LoadWorkbookDataAccessInterface {
    /**
     * @param source The file path where a serialised workbook ostensibly exists.
     * @return The serialised workbook if successfully read.
     */
    String load(Path source) throws IOException;
}
