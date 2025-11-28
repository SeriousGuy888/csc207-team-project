package use_case.display_course_context;


import use_case.ratemyprof.*;
import entity.Professor;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayCourseDetailsInteractor implements DisplayCourseDetailsInputBoundary {

    // Dependencies injected at compile time:
    private final DisplayCourseDetailsDataAccessInterface courseDetailsDAO; // Gets base course info
    private final DisplayCourseDetailsOutputBoundary presenter;
    private final RateMyProfInputBoundary rateMyProfInteractor; // Gets RMP data

    public DisplayCourseDetailsInteractor(
            DisplayCourseDetailsDataAccessInterface dataAccessObject,
            DisplayCourseDetailsOutputBoundary presenter,
            RateMyProfInputBoundary rateMyProfInteractor) { // <-- Constructor Injection
        this.courseDetailsDAO = dataAccessObject;
        this.presenter = presenter;
        this.rateMyProfInteractor = rateMyProfInteractor; // <-- Assignment
    }

    @Override
    public void execute(DisplayCourseDetailsInputData inputData) {
        // Get the specific course identifier the user clicked on.
        String courseId = inputData.getCourseOfferingId();

        // Get course description, sections, and placeholder prof name from course data.
        DisplayCourseDetails baseDetails = courseDetailsDAO.getCourseDetails(courseId);

        if (baseDetails == null) {
            presenter.prepareFailView("Course details for " + courseId + " could not be found in the repository.");
            return;
        }

        // Loop through sections and add RMP details.
        List<DisplaySectionDetails> enrichedSections = baseDetails.getSections().stream()
                .map(this::enrichSectionWithProfessorDetails)
                .collect(Collectors.toList());

        // 4. PREPARE OUTPUT: Create the final complete DTO.
        DisplayCourseDetails finalDetails = new DisplayCourseDetails(
                baseDetails.getCourseTitle(),
                baseDetails.getCourseDescription(),
                enrichedSections
        );

        // 5. SIGNAL PRESENTATION
        presenter.prepareSuccessView(new DisplayCourseDetailsOutputData(finalDetails));
    }

    /**
     * Retrieves RateMyProf data and merges it into the DisplaySectionDetails DTO.
     */
    private DisplaySectionDetails enrichSectionWithProfessorDetails(DisplaySectionDetails section) {

        String fullName = section.getProfessorDetails().getName();
        String[] parts = fullName.split(" ");
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[parts.length - 1] : "";

        RateMyProfOutputData rmpOutputData;

        // 1. Create RMP Input Data
        RateMyProfInputData rmpInput = new RateMyProfInputData(firstName, lastName);

        try {
            // 2. CALL THE SYNCHRONOUS METHOD on the injected dependency
            rmpOutputData = rateMyProfInteractor.executeSynchronous(rmpInput);
        } catch (Exception e) {
            // Handle unexpected failure by returning a default, empty professor DTO
            rmpOutputData = new RateMyProfOutputData(Professor.emptyProfessor());
        }

        // 3. Map the RMP Output Data to the Display DTO
        DisplayProfessorDetails finalProfDetails = new DisplayProfessorDetails(
                rmpOutputData.getProfFirstName() + " " + rmpOutputData.getProfLastName(),
                rmpOutputData.getAvgRating(),
                rmpOutputData.getAvgDifficultyRating(),
                rmpOutputData.getLink()
        );

        // 4. Return the enriched DTO
        return new DisplaySectionDetails(
                section.getSectionName(),
                section.getSectionId(),
                section.getMeetingTimes(),
                section.getLocation(),
                finalProfDetails
        );
    }
}