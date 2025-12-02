package interface_adapter.add_section;

import interface_adapter.GlobalViewModel;
import use_case.add_section.AddSectionInputBoundary;
import use_case.add_section.AddSectionInputData;

/**
 * Called by the View when user clicks "Add" on a section in expanded accordion view.
 
 */
public class AddSectionController {

    private final AddSectionInputBoundary interactor;
    private final GlobalViewModel globalViewModel;

    public AddSectionController(AddSectionInputBoundary interactor,
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
    public void addSection(String displayString, String sectionName) {
        int selectedTabIndex = globalViewModel.getState().getSelectedTabIndex();

        AddSectionInputData inputData = new AddSectionInputData(
                displayString,
                sectionName,
                selectedTabIndex
        );

        interactor.execute(inputData);
    }
}