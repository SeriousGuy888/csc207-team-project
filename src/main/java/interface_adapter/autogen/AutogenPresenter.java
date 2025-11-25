package interface_adapter.autogen;

import use_case.autogen.AutogenOutputBoundary;
import use_case.autogen.AutogenOutputData;

public class AutogenPresenter implements AutogenOutputBoundary{
    private final AutogenViewModel viewModel;

    public AutogenPresenter(AutogenViewModel viewModel){this.viewModel = viewModel;}

    @Override
    public void prepareSuccessView(AutogenOutputData outputData) {
        viewModel.setTimetable(outputData.getGeneratedTimetable());   // or getSchedules()

        // Set a nice message for the UI to show
        viewModel.setMessage("Successfully generated a timetable.");
        viewModel.setError(null);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setTimetable(null);
        viewModel.setMessage(null);
        viewModel.setError(errorMessage);
    }
}
