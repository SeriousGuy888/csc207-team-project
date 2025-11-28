package use_case.display_course;

import data_access.course_data.CourseDataRepository;
import entity.CourseOffering;
import entity.Section;
import entity.Meeting;
import use_case.display_course_context.DisplayCourseDetails;
import use_case.display_course_context.DisplayCourseDetailsDataAccessInterface;
import use_case.display_course_context.DisplayProfessorDetails;
import use_case.display_course_context.DisplaySectionDetails;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This DAO transforms the CourseOffering entity into the DisplayCourseDetails DTO
public class DisplayCourseDetailsDataAccessObject implements DisplayCourseDetailsDataAccessInterface {

    // Dependency on the low-level repository that fetches raw course data
    private final CourseDataRepository courseRepository;

    public DisplayCourseDetailsDataAccessObject(CourseDataRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public DisplayCourseDetails getCourseDetails(String courseId) {
        // 1. Fetch the raw entity
        CourseOffering courseOffering = courseRepository.getCourseOffering(courseId);

        if (courseOffering == null) {
            return null; // Course not found
        }

        // 2. Transform the entity data into display DTOs
        List<DisplaySectionDetails> displaySections = courseOffering.getAvailableSections().stream()
                // FlatMap converts the Set of Sections into a flat list of DisplaySectionDetails (one per Meeting)
                .flatMap(this::mapSectionToDisplayDetails)
                .collect(Collectors.toList());

        // 3. Create the final DTO
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

        // --- IMPORTANT ASSUMPTION ---
        // We assume the Section entity has a method to get the Professor's name string.
        // If not, you must implement a simple 'getProfessorName()' method in Section.
        String professorName = "TBD Professor"; // Replace with section.getProfessorName() or similar

        // Create a temporary, placeholder professor DTO that ONLY contains the name
        // (The Interactor will fill in the rating/link later)
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
                    String sectionId = section.getSectionName();

                    return new DisplaySectionDetails(
                            section.getSectionName(),
                            sectionId,
                            meetingTimes,
                            location,
                            placeholderProf // Use the placeholder here
                    );
                });
    }
}