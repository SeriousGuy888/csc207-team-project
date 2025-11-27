package interface_adapter.walktime;

import use_case.osrm_walktime.WalkTimeOutputBoundary;
import use_case.osrm_walktime.WalkTimeOutputData;

public class WalkTimePresenter implements WalkTimeOutputBoundary {

    private final WalkTimeViewModel viewModel;

    public WalkTimePresenter(WalkTimeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WalkTimeOutputData outputData) {
        WalkTimeState state = viewModel.getState();

        // --- FORMATTING LOGIC HERE ---
        int seconds = outputData.getDurationInSeconds();
        int minutes = (int) Math.ceil(seconds / 60.0);

        // Make it look nice
        String niceMessage;
        if (minutes < 1) {
            niceMessage = "Less than 1 minute";
        } else {
            niceMessage = minutes + " minutes";
        }

        state.setWalkTimeDisplay(niceMessage);
        state.setError(null);

        // Update the View
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        WalkTimeState state = viewModel.getState();
        state.setError(error);
        state.setWalkTimeDisplay(null);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}