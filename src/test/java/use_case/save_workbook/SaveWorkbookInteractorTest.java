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
    private static final String ERROR_MESSAGE_ON_FAILED_SAVE = "Failed to save file.";
    private static final Path SAVE_LOCATION = Path.of("test");
    private static final SaveWorkbookDataAccessInterface FUNCTIONAL_DAO = new SaveWorkbookDataAccessInterface() {
        @Override
        public void save(Workbook workbook, Path destination) throws IOException {
        }
    };
    private static final SaveWorkbookDataAccessInterface NONFUNCTIONAL_DAO = new SaveWorkbookDataAccessInterface() {
        @Override
        public void save(Workbook workbook, Path destination) throws IOException {
            throw new IOException(ERROR_MESSAGE_ON_FAILED_SAVE);
        }
    };

    @Test
    void getOutputDataOnSuccessfulSave() {
        SaveWorkbookOutputBoundary presenter = new SaveWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveWorkbookOutputData outputData) {
                assertEquals(SAVE_LOCATION, outputData.getDestination());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail();
            }
        };

        SaveWorkbookInputData inputData = new SaveWorkbookInputData(TestConstants.WORKBOOK_MAT137, SAVE_LOCATION);
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(FUNCTIONAL_DAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void sendErrorMessageOnIOException() {
        SaveWorkbookOutputBoundary presenter = new SaveWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveWorkbookOutputData outputData) {
                fail();
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(ERROR_MESSAGE_ON_FAILED_SAVE, errorMessage);
            }
        };

        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(NONFUNCTIONAL_DAO, presenter);
        SaveWorkbookInputData inputData = new SaveWorkbookInputData(TestConstants.WORKBOOK_MAT137, SAVE_LOCATION);
        interactor.execute(inputData);
    }
}