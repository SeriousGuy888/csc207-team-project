package interface_adapter.remove_section;

import interface_adapter.GlobalViewModel;
import use_case.remove_section.RemoveSectionInputBoundary;
import use_case.remove_section.RemoveSectionInputData;

/**
 * Called by the View when user clicks "Remove" on a section in expanded accordion view.
 
 */
public class RemoveSectionController {

    private final RemoveSectionInputBoundary interactor;
    private final GlobalViewModel globalViewModel;

    public RemoveSectionController(RemoveSectionInputBoundary interactor,
                                 GlobalViewModel globalViewModel) {
        this.interactor = interactor;
        this.globalViewModel = globalViewModel;
    }

    /**
     * Add a section to the currently selected timetable.
     *
     * @param displayString the course code from UI (e.g., "CSC207H1-F")
     * @param sectionName the section name from UI (e.g., "LEC0101")
     */
    public void removeSection(String displayString, String sectionName) {
        int selectedTabIndex = globalViewModel.getState().getSelectedTabIndex();

        RemoveSectionInputData inputData = new RemoveSectionInputData(
                displayString,
                sectionName,
                selectedTabIndex
        );

        interactor.execute(inputData);
    }
}