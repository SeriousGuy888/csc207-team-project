package use_case.load_workbook;

import java.nio.file.Path;

public class LoadWorkbookInputData {
    private final Path source;

    public LoadWorkbookInputData(Path source) {
        this.source = source;
    }

    public Path getSource() {
        return source;
    }
}
