package use_case.save_workbook;

import entity.*;
import org.junit.jupiter.api.Test;
import use_case.TestConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SaveWorkbookInteractorTest {
    @Test
    void getOutputDataOnSuccessfulSave() {
        Path path = Path.of("test");

        SaveWorkbookDataAccessInterface dao = new SaveWorkbookDataAccessInterface() {
            @Override
            public void save(Workbook workbook, Path destination) throws IOException {
            }
        };

        SaveWorkbookOutputBoundary presenter = new SaveWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveWorkbookOutputData outputData) {
                assertEquals(path, outputData.getDestination());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail();
            }
        };

        SaveWorkbookInputData inputData = new SaveWorkbookInputData(TestConstants.WORKBOOK_MAT137, path);
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(dao, presenter);
        interactor.execute(inputData);
    }

    @Test
    void sendErrorMessageOnIOException() {
        String message = "Failed to save file.";

        SaveWorkbookDataAccessInterface nonFunctionalDao = new SaveWorkbookDataAccessInterface() {
            @Override
            public void save(Workbook workbook, Path destination) throws IOException {
                throw new IOException(message);
            }
        };
        SaveWorkbookOutputBoundary presenter = new SaveWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveWorkbookOutputData outputData) {
                fail();
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(message, errorMessage);
            }
        };

        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(nonFunctionalDao, presenter);
        SaveWorkbookInputData inputData = new SaveWorkbookInputData(TestConstants.WORKBOOK_MAT137, Paths.get("test"));
        interactor.execute(inputData);
    }
}