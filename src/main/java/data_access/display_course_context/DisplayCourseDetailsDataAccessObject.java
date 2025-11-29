package data_access.display_course_context;

import data_access.course_data.CourseDataRepository;
import entity.CourseOffering;
import entity.Section;
import use_case.display_course_context.DisplayCourseDetails;
import use_case.display_course_context.DisplayCourseDetailsDataAccessInterface;
import use_case.display_course_context.DisplayProfessorDetails;
import use_case.display_course_context.DisplaySectionDetails;
import data_access.course_data.JsonCourseDataRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This DAO transforms the CourseOffering entity into the DisplayCourseDetails DTO
public class DisplayCourseDetailsDataAccessObject implements DisplayCourseDetailsDataAccessInterface {

    // Dependency on the low-level repository that fetches raw course data
    // changed the type to the concrete class now that we rely on its specific method.
    private final JsonCourseDataRepository courseRepository;

    public DisplayCourseDetailsDataAccessObject(CourseDataRepository courseRepository) {
        // We cast the injected repository to the specific type we need
        this.courseRepository = (JsonCourseDataRepository) courseRepository;
    }

    @Override
    public DisplayCourseDetails getCourseDetails(String courseId) {
        // Fetch the raw entity
        final CourseOffering courseOffering = courseRepository.getCourseOffering(courseId);

        // Course not found
        if (courseOffering == null) {
            return null;
        }

        // Transform the entity data into display DTOs
        final List<DisplaySectionDetails> displaySections = courseOffering.getAvailableSections().stream()
                // FlatMap converts the Set of Sections into a flat list of DisplaySectionDetails (one per Meeting)
                .flatMap(this::mapSectionToDisplayDetails)
                .collect(Collectors.toList());

        // Create the final DTO
        return new DisplayCourseDetails(
                courseOffering.getTitle(),
                courseOffering.getDescription(),
                displaySections
        );
    }

    /**
     * Maps one Section entity into a stream of DisplaySectionDetails,
     * generating one DTO for every single Meeting time slot in that section.
     * @param section is the Section entity we are looking for details about.
     */
    private Stream<DisplaySectionDetails> mapSectionToDisplayDetails(Section section) {

        final String sectionId = section.getSectionName();
        // Call the lookup method using the section's unique name
        final String professorName = getProfessorNameBySectionId(sectionId);

        // Create the placeholder professor DTO that ONLY contains the name
        // The Interactor will fill in the rating/link later
        final DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                professorName,
                0.0,
                0.0,
                null
        );

        // Convert entity objects to simple display strings
        return section.getMeetings().stream()
                .map(meeting -> {
                    final String meetingTimes = meeting.getTime().toString();
                    final String location = meeting.getLocation().toString();
                    return new DisplaySectionDetails(
                            section.getSectionName(),
                            sectionId,
                            meetingTimes,
                            location,
                            placeholderProf
                    ); });
    }

    @Override
    public String getProfessorNameBySectionId(String sectionId) {
        return courseRepository.getProfessorNameBySectionId(sectionId);
    }
}