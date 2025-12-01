package interface_adapter.save_workbook;

import use_case.save_workbook.SaveWorkbookOutputBoundary;
import use_case.save_workbook.SaveWorkbookOutputData;

import java.nio.file.Path;

public class SaveWorkbookPresenter implements SaveWorkbookOutputBoundary {
    private final SaveWorkbookViewModel viewModel;

    public SaveWorkbookPresenter(SaveWorkbookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveWorkbookOutputData outputData) {
        final Path destination = outputData.getDestination();
        final SaveWorkbookState state = new SaveWorkbookState(true,
                "Saved workbook to " + destination.toString()
        );
        viewModel.setState(state);
        viewModel.firePropertyChange(SaveWorkbookViewModel.STATE_PROPERTY_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SaveWorkbookState state = new SaveWorkbookState(false, errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange(SaveWorkbookViewModel.STATE_PROPERTY_NAME);
    }
}
