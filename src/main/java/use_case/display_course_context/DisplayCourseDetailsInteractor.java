package use_case.display_course_context;


import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayCourseDetails;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplayProfessorDetails;
import use_case.display_course_context.display_course_details_data_transfer_objects.DisplaySectionDetails;
import use_case.ratemyprof.*;
import entity.Professor;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayCourseDetailsInteractor implements DisplayCourseDetailsInputBoundary {

    private final DisplayCourseDetailsDataAccessInterface courseDetailsDao;
    private final DisplayCourseDetailsOutputBoundary presenter;
    private final RateMyProfInputBoundary rateMyProfInteractor;

    public DisplayCourseDetailsInteractor(
            DisplayCourseDetailsDataAccessInterface dataAccessObject,
            DisplayCourseDetailsOutputBoundary presenter,
            RateMyProfInputBoundary rateMyProfInteractor) {
        this.courseDetailsDao = dataAccessObject;
        this.presenter = presenter;
        this.rateMyProfInteractor = rateMyProfInteractor;
    }

    @Override
    public void execute(DisplayCourseDetailsInputData inputData) {
        // Get the specific course identifier the user clicked on.
        final String courseId = inputData.getCourseOfferingId();

        // Get course description, sections, and placeholder prof name from course data.
        final DisplayCourseDetails baseDetails = courseDetailsDao.getCourseDetails(courseId);

        if (baseDetails == null) {
            presenter.prepareFailView(courseId,"Course details for " + courseId + " could not be found.");
            return;
        }

        // Loop through sections and add RMP details.
        final List<DisplaySectionDetails> enrichedSections = baseDetails.getSections().stream()
                .map(this::enrichSectionWithProfessorDetails)
                .collect(Collectors.toList());

        // PREPARE OUTPUT: Create the final complete DTO.
        final DisplayCourseDetails finalDetails = new DisplayCourseDetails(
                baseDetails.getCourseId(),
                baseDetails.getCourseTitle(),
                baseDetails.getCourseDescription(),
                enrichedSections
        );

        // SIGNAL PRESENTATION
        presenter.prepareSuccessView(new DisplayCourseDetailsOutputData(finalDetails));
    }

    /**
     * Retrieves RateMyProf data and merges it into the DisplaySectionDetails DTO.
     * @param section the Section we are finding the professor for.
     */
    private DisplaySectionDetails enrichSectionWithProfessorDetails(DisplaySectionDetails section) {

        final String fullName = section.getProfessorDetails().getName();
        final String[] parts = fullName.split(" ");
        final String firstName = parts.length > 0 ? parts[0] : "";
        final String lastName = parts.length > 1 ? parts[parts.length - 1] : "";

        RateMyProfOutputData rmpOutputData;

        // Create RMP Input Data
        final RateMyProfInputData rmpInput = new RateMyProfInputData(firstName, lastName);

        try {
            // CALL THE SYNCHRONOUS METHOD on the injected dependency
            rmpOutputData = rateMyProfInteractor.executeSynchronous(rmpInput);
        } catch (Exception e) {
            // Handle unexpected failure by returning a default, empty professor DTO
            rmpOutputData = new RateMyProfOutputData(Professor.emptyProfessor());
        }

        // Map the RMP Output Data to the Display DTO
        final DisplayProfessorDetails finalProfDetails = new DisplayProfessorDetails(
                rmpOutputData.getProfFirstName() + " " + rmpOutputData.getProfLastName(),
                rmpOutputData.getAvgRating(),
                rmpOutputData.getAvgDifficultyRating(),
                rmpOutputData.getLink()
        );

        // Return the "enriched" (has prof info now) DTO
        return new DisplaySectionDetails(
                section.getSectionName(),
                section.getMeetingTimes(),
                finalProfDetails
        );
    }
}