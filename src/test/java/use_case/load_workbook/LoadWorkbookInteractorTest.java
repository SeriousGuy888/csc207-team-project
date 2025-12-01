package use_case.load_workbook;

import data_access.WorkbookDataAccessObject;
import data_access.workbook_instance.TestWorkbookDataAccessObject;
import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.TestConstants;
import use_case.WorkbookDataAccessInterface;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LoadWorkbookInteractorTest {
    private static final Path SAVED_LOCATION = Path.of("correct");
    private static final Path INCORRECT_LOCATION = Path.of("incorrect");
    private static final String ERROR_MESSAGE_WHEN_NONEXISTENT = "Not found.";
    private static final Workbook DESIRED_WORKBOOK = TestConstants.WORKBOOK_MAT137;

    private static final TestWorkbookDataAccessObject DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK =
            new TestWorkbookDataAccessObject();
    private static final LoadWorkbookDataAccessInterface FAKE_DAO = new LoadWorkbookDataAccessInterface() {
        @Override
        public Workbook load(Path source) throws IOException {
            if (source == SAVED_LOCATION) {
                return DESIRED_WORKBOOK;
            }
            throw new IOException(ERROR_MESSAGE_WHEN_NONEXISTENT);
        }
    };

    @Test
    void successfulLoad() {
        DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK.reset();

        LoadWorkbookOutputBoundary presenter = new LoadWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadWorkbookOutputData outputData) {
                assertEquals(DESIRED_WORKBOOK, DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK.getWorkbook());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail();
            }
        };

        LoadWorkbookInputData inputData = new LoadWorkbookInputData(SAVED_LOCATION);
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(
                DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK,
                FAKE_DAO,
                presenter);
        interactor.execute(inputData);
    }

    @Test
    void unsuccessfulLoadDisplaysErrorMessage() {
        DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK.reset();

        LoadWorkbookOutputBoundary presenter = new LoadWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadWorkbookOutputData outputData) {
                fail();
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(ERROR_MESSAGE_WHEN_NONEXISTENT, errorMessage);
            }
        };

        LoadWorkbookInputData inputData = new LoadWorkbookInputData(INCORRECT_LOCATION);
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(
                DAO_FOR_CURRENTLY_ACTIVE_WORKBOOK,
                FAKE_DAO,
                presenter);
        interactor.execute(inputData);
    }
}
