package interface_adapter;

import java.util.ArrayList;
import java.util.List;

import entity.CourseCode;
import entity.Meeting;
import entity.Section;
import entity.Timetable;
import entity.Workbook;
import interface_adapter.TimetableState.MeetingBlock;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputData;
import use_case.tab_actions.add_tab.AddTabOutputBoundary;
import use_case.tab_actions.delete_tab.DeleteTabOutputBoundary;
import use_case.tab_actions.rename_tab.RenameTabOutputBoundary;
import use_case.tab_actions.switch_tab.SwitchTabOutputBoundary;
import java.util.stream.Collectors;
import interface_adapter.TimetableState.SelectedSectionRow;

public class GlobalViewPresenter implements
        TimetableUpdateOutputBoundary,
        AddTabOutputBoundary,
        DeleteTabOutputBoundary,
        SwitchTabOutputBoundary,
        RenameTabOutputBoundary {

    // Constants for mapping Entity time to UI Grid
    private static final int START_HOUR_INDEX = 18;
    private static final int HALF_HOUR_SLOTS_PER_DAY = 24;

    private final GlobalViewModel globalViewModel;

    public GlobalViewPresenter(GlobalViewModel globalViewModel) {
        this.globalViewModel = globalViewModel;
    }

    // --- HANDLE ADD / DELETE / RENAME ---
    @Override
    public void prepareSuccessView(Workbook workbook) {
        final GlobalViewState state = globalViewModel.getState();

        // 1. Rebuild the entire list of TimetableStates
        // (Since a tab was added/removed, indices have shifted)
        final List<TimetableState> newStates = new ArrayList<>();
        final List<TimetableState> oldList = state.getTimetableStateList();

        for (Timetable t : workbook.getTimetables()) {
            // Reuse conversion helper
            newStates.add(convertEntityToState(t));
        }

        state.setTimetableStateList(newStates);

        // Adjust selection if out of bounds (e.g. deleted last tab)
        if (newStates.size() > oldList.size()) {
            state.setSelectedTabIndex(newStates.size() - 1);
        }
        else if (state.getSelectedTabIndex() >= newStates.size()) {
            state.setSelectedTabIndex(Math.max(0, newStates.size() - 1));
        }

        globalViewModel.setState(state);
        globalViewModel.firePropertyChange(GlobalViewModel.TIMETABLE_CHANGED);
    }

    /**
     * Switches to a different tab in the UI.
     * @param newIndex the index of the tab to switch to
     */
    @Override
    public void prepareSuccessView(int newIndex) {
        final GlobalViewState state = globalViewModel.getState();
        state.setSelectedTabIndex(newIndex);
        globalViewModel.setState(state);
        globalViewModel.firePropertyChange(GlobalViewModel.TIMETABLE_CHANGED);
    }

    /**
     * Refresh timetable on given index.
     * @param timetableOutputData The output data from the use case.
     */
    @Override
    public void prepareSuccessView(TimetableUpdateOutputData timetableOutputData) {
        final Timetable changedTimetable = timetableOutputData.getTimetable();
        final int tabIndex = timetableOutputData.getTabIndex();
        updateSingleTimetable(changedTimetable, tabIndex);
    }

    /**
     * Updates a single timetable in the UI.
     * @param changedTimetable The timetable that was changed.
     * @param tabIndex The index of the timetable in the UI.
     */
    public void updateSingleTimetable(Timetable changedTimetable, int tabIndex) {
        final GlobalViewState globalState = globalViewModel.getState();
        final List<TimetableState> currentStates = globalState.getTimetableStateList();

        // 1. Safety Check: Ensure the index is valid
        if (tabIndex < 0 || tabIndex >= currentStates.size()) {
            // Fallback: If index is weird, reload the whole thing (safety net)
            return;
        }

        // 2. Convert ONLY the changed entity to a new State
        final TimetableState newTabState = convertEntityToState(changedTimetable);

        // 3. Swap it into the existing list
        currentStates.set(tabIndex, newTabState);

        // 4. Update Global State
        globalState.setTimetableStateList(currentStates);
        globalViewModel.setState(globalState);

        // 5. Fire Event
        // We can pass the index as the "property name" or part of the object if we want strictly optimized listening,
        // but usually firing the standard state change is fine if the View handles it smartly.
        globalViewModel.firePropertyChange(GlobalViewModel.TIMETABLE_CHANGED);
    }

    /**
     * Converts a Timetable entity to a TimetableState object.
     * @param timetable The timetable to convert.
     * @return The converted TimetableState.
     */
    private TimetableState convertEntityToState(Timetable timetable) {
        final TimetableState state = new TimetableState();
        state.setTimetableName(timetable.getTimetableName());

        final MeetingBlock[][][] firstSemesterGrid = state.getFirstSemesterGrid();
        final MeetingBlock[][][] secondSemesterGrid = state.getSecondSemesterGrid();

        // 1. Calculate Conflicts inside the Entity
        // (Ensures the Presenter just reads flags, doesn't do business math)
        timetable.markConflicts();

        // 2. Iterate through every section and meeting
        for (Section section : timetable.getSections()) {
            final CourseCode courseCode = section.getCourseOffering().getCourseCode();
            final String sectionInfo = section.getSectionName();

            for (Meeting meeting : section.getMeetings()) {
                final int startRow = meeting.getStartTimeIndexInDay() - START_HOUR_INDEX;
                final boolean isConflict = meeting.getNumConflicts() > 0;
                final MeetingBlock block = new MeetingBlock(courseCode.toString(), sectionInfo, startRow, isConflict);

                // 3. Map Meeting time to Grid Coordinates
                if (meeting.getSemester() == Meeting.Semester.FIRST) {
                    placeBlockInGrid(firstSemesterGrid, meeting, block);
                }
                else {
                    placeBlockInGrid(secondSemesterGrid, meeting, block);
                }

            }
        }

        //3. Add Lock Section Table
        final java.util.List<SelectedSectionRow> rows = timetable.getSections().stream()
                .map(section -> new SelectedSectionRow(
                        section.getCourseOffering().getCourseCode().toString(),
                        section.getSectionName(),
                        section.getTeachingMethod().toString(),
                        timetable.isLocked(section)
                ))
                .collect(Collectors.toList());

        state.setSelectedSections(rows);
        return state;
    }

    /**
     * Helper method to place a MeetingBlock in the correct slot in the grid. This function prevents jagged blocks
     * by predetermining the index to insert the block.
     * @param grid The grid to place the meeting block in.
     * @param meeting The meeting that the block belongs to.
     * @param block The block to place.
     */
    private void placeBlockInGrid(MeetingBlock[][][] grid, Meeting meeting, MeetingBlock block) {
        final int col = meeting.getStartTimeIndexInDay();
        final int startRow = block.getStartRow();
        int blockIndex = 0;

        // determine the index to insert the meeting blocks
        if (grid[startRow][col][0] != null) {
            blockIndex = 1;
        }

        for (int row = block.getStartRow(); row < HALF_HOUR_SLOTS_PER_DAY; row++) {
            final int timeSlotIndex = START_HOUR_INDEX + row;

            if (meeting.checkOccupancy(col, timeSlotIndex)) {
                grid[row][col][blockIndex] = block;
            }
        }
    }

    /**
     * Executes when update failed.
     * @param error The error message to display
     */
    @Override
    public void prepareFailView(String error) {
        // TODO: Handle error
        System.out.println(error);
    }
}
