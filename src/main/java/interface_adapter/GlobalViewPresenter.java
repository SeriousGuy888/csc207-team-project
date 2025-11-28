package interface_adapter;

import entity.*;
import interface_adapter.TimetableState.MeetingBlock;
import use_case.TimetableUpdate.TimetableUpdateOutputBoundary;
import use_case.TimetableUpdate.TimetableUpdateOutputData;

import java.util.List;

public class GlobalViewPresenter implements TimetableUpdateOutputBoundary {

    private final GlobalViewModel globalViewModel;

    // Constants for mapping Entity time to UI Grid
    private static final int START_HOUR_INDEX = 18; // 9:00 AM is index 18 in WeeklyOccupancy
    private static final int END_HOUR_INDEX = 42;   // 9:00 PM is index 42

    public GlobalViewPresenter(GlobalViewModel globalViewModel) {
        this.globalViewModel = globalViewModel;
    }

    /**
     * @param timetableOutputData
     */
    @Override
    public void prepareSuccessView(TimetableUpdateOutputData timetableOutputData) {
        Timetable changedTimetable = timetableOutputData.getTimetable();
        int tabIndex = timetableOutputData.getTabIndex();
        updateSingleTimetable(changedTimetable, tabIndex);
    }

    public void updateSingleTimetable(Timetable changedTimetable, int tabIndex) {
        GlobalViewState globalState = globalViewModel.getState();
        List<TimetableState> currentStates = globalState.getTimetableStateList();

        // 1. Safety Check: Ensure the index is valid
        if (tabIndex < 0 || tabIndex >= currentStates.size()) {
            // Fallback: If index is weird, reload the whole thing (safety net)
            // prepareSuccessView(workbook);
            return;
        }

        // 2. Convert ONLY the changed entity to a new State
        TimetableState newTabState = convertEntityToState(changedTimetable);

        // 3. Swap it into the existing list
        // We modify the existing list to avoid reconstructing the whole GlobalState
        currentStates.set(tabIndex, newTabState);

        // 4. Update Global State
        globalState.setTimetableStateList(currentStates);
        globalViewModel.setState(globalState);

        // 5. Fire Event
        // We can pass the index as the "property name" or part of the object if we want strictly optimized listening,
        // but usually firing the standard state change is fine if the View handles it smartly.
        globalViewModel.fireStateChangeEvent();
    }

    private TimetableState convertEntityToState(Timetable timetable) {
        TimetableState state = new TimetableState();
        List<MeetingBlock>[][] firstSemesterGrid = state.getFirstSemesterGrid();
        List<MeetingBlock>[][] secondSemesterGrid = state.getSecondSemesterGrid();


        // 1. Calculate Conflicts inside the Entity
        // (Ensures the Presenter just reads flags, doesn't do business math)
        timetable.markConflicts();

        // 2. Iterate through every section and meeting
        for (Section section : timetable.getSections()) {
            CourseCode courseCode = section.getCourseOffering().getCourseCode(); // Adjust based on your Entity
            String sectionInfo = section.getSectionName();

            for (Meeting meeting : section.getMeetings()) {
                boolean isConflict = meeting.getNumConflicts() > 0;
                TimetableState.MeetingBlock block = new MeetingBlock(courseCode.toString(), sectionInfo, isConflict);

                // 3. Map Meeting time to Grid Coordinates
                if (meeting.getSemester() == Meeting.Semester.FIRST) {
                    placeBlockInGrid(firstSemesterGrid, meeting, block);
                }
                else {
                    placeBlockInGrid(secondSemesterGrid, meeting, block);
                }

            }
        }
        return state;
    }

    private void placeBlockInGrid(List<MeetingBlock>[][] grid, Meeting meeting, MeetingBlock block) {
        // Loop through the UI grid slots (0-23)
        for (int row = 0; row < 24; row++) {
            // Convert UI row back to Entity bit index
            int bitIndex = row + START_HOUR_INDEX;

            for (int col = 0; col < 5; col++) { // Mon(0) - Fri(4)

                if (meeting.checkOccupancy(col, bitIndex)) {
                    grid[row][col].add(block);
                }
            }
        }
    }

    /**
     * @param error
     */
    @Override
    public void prepareFailView(String error) {
        System.out.println(error);
    }
}