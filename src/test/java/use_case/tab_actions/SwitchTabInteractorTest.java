package use_case.tab_actions;

import org.junit.jupiter.api.Test;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class SwitchTabInteractorTest {

    @Test
    void executePassesIndexToPresenter() {
        // Arrange
        final int targetIndex = 5;

        // Use a 1-element array as a wrapper to capture the result from the lambda
        final int[] capturedIndex = new int[1];

        SwitchTabOutputBoundary presenter = new SwitchTabOutputBoundary() {
            @Override
            public void prepareSuccessView(int newIndex) {
                capturedIndex[0] = newIndex;
            }
        };

        SwitchTabInteractor interactor = new SwitchTabInteractor(presenter);

        // Act
        interactor.execute(targetIndex);

        // Assert
        assertEquals(targetIndex, capturedIndex[0]);
    }
}
