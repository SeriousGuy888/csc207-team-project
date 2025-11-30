package use_case.save_workbook;

import entity.Workbook;

import java.nio.file.Path;

public class SaveWorkbookInputData {
    private final Workbook workbook;
    private final Path destination;

    public SaveWorkbookInputData(Workbook workbook, Path destination) {
        this.workbook = workbook;
        this.destination = destination;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Path getDestination() {
        return destination;
    }
}
