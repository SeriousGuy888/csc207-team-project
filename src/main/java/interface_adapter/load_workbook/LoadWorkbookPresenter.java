package interface_adapter.load_workbook;

import interface_adapter.save_workbook.SaveWorkbookViewModel;
import use_case.load_workbook.LoadWorkbookOutputBoundary;
import use_case.load_workbook.LoadWorkbookOutputData;

public class LoadWorkbookPresenter implements LoadWorkbookOutputBoundary {
    private final LoadWorkbookViewModel viewModel;

    public LoadWorkbookPresenter(LoadWorkbookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadWorkbookOutputData outputData) {
        final LoadWorkbookState state = new LoadWorkbookState(true, outputData.getMessage());
        viewModel.setState(state);
        viewModel.firePropertyChange(LoadWorkbookViewModel.STATE_PROPERTY_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoadWorkbookState state = new LoadWorkbookState(false, errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange(LoadWorkbookViewModel.STATE_PROPERTY_NAME);
    }
}
