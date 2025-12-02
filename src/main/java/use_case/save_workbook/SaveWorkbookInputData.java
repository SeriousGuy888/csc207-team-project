package use_case.save_workbook;

import entity.Workbook;

import java.nio.file.Path;

public class SaveWorkbookInputData {
    private final Path destination;

    public SaveWorkbookInputData(Path destination) {
        this.destination = destination;
    }

    public Path getDestination() {
        return destination;
    }
}
