package use_case.load_workbook;

import entity.Workbook;
import org.junit.jupiter.api.Test;
import use_case.TestConstants;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LoadWorkbookInteractorTest {
    private static final Path SAVED_LOCATION = Path.of("correct");
    private static final Path INCORRECT_LOCATION = Path.of("incorrect");
    private static final Workbook DESIRED_WORKBOOK = TestConstants.WORKBOOK_MAT137;
    private static final String ERROR_MESSAGE_WHEN_NONEXISTENT = "Not found.";
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
        LoadWorkbookOutputBoundary presenter = new LoadWorkbookOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadWorkbookOutputData outputData) {
                assertEquals(DESIRED_WORKBOOK, outputData.getLoadedWorkbook());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail();
            }
        };

        LoadWorkbookInputData inputData = new LoadWorkbookInputData(SAVED_LOCATION);
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(FAKE_DAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void unsuccessfulLoadDisplaysErrorMessage() {
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
        LoadWorkbookInteractor interactor = new LoadWorkbookInteractor(FAKE_DAO, presenter);
        interactor.execute(inputData);
    }
}
