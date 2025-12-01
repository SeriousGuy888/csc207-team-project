package use_case.save_workbook;

import data_access.WorkbookDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Test;
import use_case.TestConstants;
import use_case.WorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SaveWorkbookInteractorTest {
    private static final String ERROR_MESSAGE_ON_FAILED_SAVE = "Failed to save file.";
    private static final Path SAVE_LOCATION = Path.of("test");
    private static final WorkbookDataAccessInterface DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK =
            new WorkbookDataAccessInterface() {
                // todo: make this copy the workbook instead of just using the one that's supposed to be constant
                private Workbook workbook = TestConstants.WORKBOOK_MAT137;

                @Override
                public Workbook getWorkbook() {
                    return workbook;
                }

                @Override
                public void saveWorkbook(Workbook workbook) {
                    this.workbook = workbook;
                }
            };
    private static final SaveWorkbookDataAccessInterface FUNCTIONAL_SAVING_DAO =
            new SaveWorkbookDataAccessInterface() {
                @Override
                public void save(Workbook workbook, Path destination) throws IOException {
                }
            };
    private static final SaveWorkbookDataAccessInterface NONFUNCTIONAL_SAVING_DAO =
            new SaveWorkbookDataAccessInterface() {
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

        SaveWorkbookInputData inputData = new SaveWorkbookInputData(SAVE_LOCATION);
        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(
                DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK,
                FUNCTIONAL_SAVING_DAO,
                presenter);
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
            }
        };

        SaveWorkbookInteractor interactor = new SaveWorkbookInteractor(
                DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK,
                NONFUNCTIONAL_SAVING_DAO,
                presenter);
        SaveWorkbookInputData inputData = new SaveWorkbookInputData(SAVE_LOCATION);
        interactor.execute(inputData);
    }
}