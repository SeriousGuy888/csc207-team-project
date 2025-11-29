package interface_adapter.add_section;

import interface_adapter.GlobalViewPresenter;
import use_case.addsection.AddSectionOutputBoundary;
import use_case.addsection.AddSectionOutputData;

/**
 * Presenter for the Add Section use case.
 * Updates the view through GlobalViewPresenter.
 */
public class AddSectionPresenter implements AddSectionOutputBoundary {

    private final GlobalViewPresenter globalViewPresenter;

    public AddSectionPresenter(GlobalViewPresenter globalViewPresenter) {
        this.globalViewPresenter = globalViewPresenter;
    }

    @Override
    public void prepareSuccessView(AddSectionOutputData outputData) {
        globalViewPresenter.updateSingleTimetable(
                outputData.getTimetable(),
                outputData.getTabIndex()
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO: Show error to user (dialog, toast, etc.)
        System.err.println("Add section failed: " + errorMessage);
    }
}
