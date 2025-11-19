package interface_adapter.autogen;

import use_case.autogen.AutogenOutputBoundary;
import use_case.autogen.AutogenOutputData;

public class AutogenPresenter implements AutogenOutputBoundary{
    private final AutogenViewModel viewModel;

    public AutogenPresenter(AutogenViewModel viewModel){this.viewModel = viewModel;}

    @Override
    public void prepareSuccessView(AutogenOutputData outputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println(errorMessage);
        viewModel.setTimetable(null);
        viewModel.setMessage(null);
        viewModel.setError(errorMessage);
    }
}
