package interface_adapter.locksections;

import entity.Section;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewState;
import interface_adapter.TimetableState;
import use_case.locksections.LockSectionsOutputBoundary;
import use_case.locksections.LockSectionsOutputData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Presenter for the Lock Sections use case.
 * Updates the TimetableState of the currently selected tab
 * with labels of the locked sections.
 */
public class LockSectionsPresenter implements LockSectionsOutputBoundary {

    private final GlobalViewModel globalViewModel;

    public LockSectionsPresenter(GlobalViewModel globalViewModel) {
        this.globalViewModel = globalViewModel;
    }

    @Override
    public void prepareSuccessView(LockSectionsOutputData outputData) {
        GlobalViewState globalState = globalViewModel.getState();

        int selectedIndex = globalState.getSelectedTabIndex();
        List<TimetableState> timetableStates = globalState.getTimetableStateList();

        // Safety check: if no valid tab is selected, do nothing
        if (selectedIndex < 0 || selectedIndex >= timetableStates.size()) {
            return;
        }

        TimetableState currentTabState = timetableStates.get(selectedIndex);

        // Convert Sections -> String labels like "CSC207 LEC0101"
        Set<String> lockedLabels = new HashSet<>();
        for (Section section : outputData.getLockedSections()) {
            String label = buildLabel(section);
            lockedLabels.add(label);
        }

        currentTabState.setLockedSectionLabels(lockedLabels);

        // Write back into global state and notify listeners
        globalState.setTimetableStateList(timetableStates);
        globalViewModel.setState(globalState);
        globalViewModel.firePropertyChange(GlobalViewModel.TIMETABLE_CHANGED);
    }

    /**
     * Builds the display label for a section, e.g. "CSC207 LEC0101".
     */
    private String buildLabel(Section section) {
        String course = section.getCourseOffering().getCourseCode().toString();
        String sect = section.getSectionName();
        return course + " " + sect;
    }
}
