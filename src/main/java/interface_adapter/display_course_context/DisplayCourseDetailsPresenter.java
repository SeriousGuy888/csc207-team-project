package interface_adapter.display_course_context;

import use_case.display_course_context.DisplayCourseDetailsOutputBoundary;
import use_case.display_course_context.DisplayCourseDetailsOutputData;
import use_case.display_course_context.DisplayCourseDetails;
import use_case.display_course_context.DisplaySectionDetails;
import use_case.display_course_context.DisplayProfessorDetails;

import java.util.List;
import java.util.stream.Collectors;

// TODO: Add this when viewModel done
// final DisplayCourseDetailsViewModel viewModel;

public class DisplayCourseDetailsPresenter implements DisplayCourseDetailsOutputBoundary {

    // Assume injection of the ViewModel or ViewUpdater here
    // public DisplayCourseDetailsPresenter(DisplayCourseDetailsViewModel viewModel) {
    //     this.viewModel = viewModel;
    // }

    /**
     * Handles the successful result from the Interactor.
     * Converts the Output Data DTOs into presentation-friendly strings/models.
     */
    @Override
    public void prepareSuccessView(DisplayCourseDetailsOutputData output) {

        DisplayCourseDetails details = output.getCourseDetails();

        // Course title
        String title = details.getCourseTitle();
        String description = details.getCourseDescription();

        // Format sections for the UI (list of strings)
        List<String> formattedSections = details.getSections().stream()
                .map(this::formatSectionForDisplay)
                .collect(Collectors.toList());

        // Update the ViewModel/View state
        // viewModel.updateState(title, description, formattedSections);
        // viewModel.firePropertyChanged();

    }

    /**
     * Called by the Interactor if data fetching fails.
     */
    @Override
    public void prepareFailView(String error) {
        // Log the error and update the view model to show an error message
        // viewModel.updateError(error);
        // viewModel.firePropertyChanged();
    }

    /**
     * Helper method to format a single section's details into a readable string for the UI.
     */
    private String formatSectionForDisplay(DisplaySectionDetails section) {
        DisplayProfessorDetails prof = section.getProfessorDetails();

        String profString = String.format("%s (RMP: %.1f, Diff: %.1f)",
                prof.getName(),
                prof.getAvgRating(),
                prof.getAvgDifficultyRating());

        return String.format(
                "%s | Time: %s | Location: %s | Prof: %s",
                section.getSectionName(),
                profString
        );
    }
}