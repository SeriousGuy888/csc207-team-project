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
    // We change the type to the concrete class now that we rely on its specific method.
    private final JsonCourseDataRepository courseRepository; // <-- CHANGED TYPE

    public DisplayCourseDetailsDataAccessObject(CourseDataRepository courseRepository) {
        // We cast the injected repository to the specific type we need
        this.courseRepository = (JsonCourseDataRepository) courseRepository; // <-- CAST HERE
    }

    @Override
    public DisplayCourseDetails getCourseDetails(String courseId) {
        // Fetch the raw entity
        CourseOffering courseOffering = courseRepository.getCourseOffering(courseId);

        if (courseOffering == null) {
            return null; // Course not found
        }

        // Transform the entity data into display DTOs
        List<DisplaySectionDetails> displaySections = courseOffering.getAvailableSections().stream()
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
     */
    private Stream<DisplaySectionDetails> mapSectionToDisplayDetails(Section section) {

        String sectionId = section.getSectionName();
        // Call the lookup method using the section's unique name
        String professorName = getProfessorNameBySectionId(sectionId);

        // Create the placeholder professor DTO that ONLY contains the name
        // The Interactor will fill in the rating/link later
        DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                professorName,
                0.0, // Default/Unknown rating
                0.0, // Default/Unknown difficulty
                null // Default/Unknown link
        );

        return section.getMeetings().stream()
                .map(meeting -> {
                    // Convert complex entity objects to simple display strings
                    String meetingTimes = meeting.getTime().toString();
                    String location = meeting.getLocation().toString();

                    // Note: sectionId is already defined above, but we reuse it here
                    // String sectionId = section.getSectionName();

                    return new DisplaySectionDetails(
                            section.getSectionName(),
                            sectionId,
                            meetingTimes,
                            location,
                            placeholderProf // Use the placeholder here
                    );
                });
    }

    @Override
    public String getProfessorNameBySectionId(String sectionId) {
        // FIX: We now call the new method implemented in JsonCourseDataRepository
        return courseRepository.getProfessorNameBySectionId(sectionId);
    }
}