package interface_adapter.walktime;

import use_case.WalkTimeOutputBoundary;
import use_case.WalkTimeOutputData;

public class WalkTimePresenter implements WalkTimeOutputBoundary {

    private final WalkTimeViewModel walkTimeViewModel;

    public WalkTimePresenter(WalkTimeViewModel walkTimeViewModel) {
        this.walkTimeViewModel = walkTimeViewModel;
    }

    @Override
    public void prepareSuccessView(WalkTimeOutputData output) {
        WalkTimeState state = walkTimeViewModel.getState();

        // Format the integer into a nice string
        String message = "Walk: " + output.getWalkTimeInMinutes() + " mins";

        state.setWalkTimeDisplay(message);
        state.setError(null); // Clear any previous errors

        walkTimeViewModel.setState(state);
        walkTimeViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        WalkTimeState state = walkTimeViewModel.getState();

        state.setError(error);
        state.setWalkTimeDisplay("Walk: Unknown"); // Fallback display

        walkTimeViewModel.setState(state);
        walkTimeViewModel.firePropertyChanged();
    }
}