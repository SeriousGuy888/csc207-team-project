package use_case.save_workbook;

import java.nio.file.Path;

public class SaveWorkbookOutputData {
    private final Path destination;

    public SaveWorkbookOutputData(Path destination) {
        this.destination = destination;
    }

    public Path getDestination() {
        return destination;
    }
}
