package interface_adapter.display_course_context;

import use_case.display_course_context.DisplayCourseDetailsOutputBoundary;
import use_case.display_course_context.DisplayCourseDetailsOutputData;

import javax.swing.*;

public class DisplayCourseDetailsPresenter implements DisplayCourseDetailsOutputBoundary {

    private final DisplayCourseDetailsViewModel displayCoursesViewModel;

    public DisplayCourseDetailsPresenter(DisplayCourseDetailsViewModel displayCoursesViewModel) {
        this.displayCoursesViewModel = displayCoursesViewModel;
    }

    @Override
    public void prepareSuccessView(DisplayCourseDetailsOutputData outputData) {
        // Format data into a State object
        DisplayCourseDetailsState newState = new DisplayCourseDetailsState(
                outputData.getCourseDetails().getCourseId(),
                outputData.getCourseDetails().getCourseTitle(),
                outputData.getCourseDetails().getCourseDescription(),
                outputData.getCourseDetails().getSections(),
                null
        );

        displayCoursesViewModel.setState(newState);
    }

    @Override
    public void prepareFailView(String failedCourseId, String error) {
        // If the use case fails, update the ViewModel and show a popup message.
        DisplayCourseDetailsState errorState = new DisplayCourseDetailsState(failedCourseId, null, null, null, error);
        displayCoursesViewModel.setState(errorState);

        // Show an error dialog to the user
        JOptionPane.showMessageDialog(null, "Error loading course details: " + error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}