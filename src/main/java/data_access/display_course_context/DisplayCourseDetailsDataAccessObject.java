package data_access.display_course_context;

import data_access.course_data.CourseDataRepository;
import entity.CourseOffering;
import entity.Section;
import use_case.display_course_context.DisplayCourseDetails;
import use_case.display_course_context.DisplayCourseDetailsDataAccessInterface;
import use_case.display_course_context.DisplayProfessorDetails;
import use_case.display_course_context.DisplaySectionDetails;
import data_access.course_data.JsonCourseDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This DAO transforms the CourseOffering entity into the DisplayCourseDetails DTO
public class DisplayCourseDetailsDataAccessObject implements DisplayCourseDetailsDataAccessInterface {

    private final JsonCourseDataRepository courseRepository;

    public DisplayCourseDetailsDataAccessObject(CourseDataRepository courseRepository) {
        // We cast the injected repository to the specific type we need
        this.courseRepository = (JsonCourseDataRepository) courseRepository;
    }

    @Override
    public DisplayCourseDetails getCourseDetails(String courseId) {
        // 1. Get the department code (first 3 letters) from the simplified courseId
        String deptCode = courseId.substring(0, 3).toUpperCase();

        // 2. Fetch all known course offerings for that department (from the grouped repository)
        // We assume getMatchingCourseInfo(deptCode) returns Map<String (Full ID), CourseOffering>
        java.util.Map<String, CourseOffering> deptOfferings =
                courseRepository.getMatchingCourseInfo(deptCode);

        if (deptOfferings == null || deptOfferings.isEmpty()) {
            return null; // Department not found or no offerings
        }

        // 3. Filter offerings to find all versions of the requested course (e.g., ABP102Y1F, ABP102Y1S)
        List<CourseOffering> matchingOfferings = deptOfferings.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(courseId))
                .map(java.util.Map.Entry::getValue)
                .collect(Collectors.toList());

        // If no matching offerings are found, return null
        if (matchingOfferings.isEmpty()) {
            return null;
        }

        // --- Aggregation ---

        // Use the first match to get the title and description (which should be the same across all terms)
        final CourseOffering primaryOffering = matchingOfferings.get(0);

        // 4. Combine ALL sections from ALL matching course offerings
        final List<DisplaySectionDetails> allDisplaySections = matchingOfferings.stream()
                .flatMap(offering -> offering.getAvailableSections().stream())
                .distinct() // Avoid duplicate sections if they appear in multiple offerings (rare, but safe)
                .flatMap(this::mapSectionToDisplayDetails)
                .collect(Collectors.toList());

        // 5. Create the final DTO with combined data
        return new DisplayCourseDetails(
                courseId, // Use the simple ID as the context ID
                primaryOffering.getTitle(),
                primaryOffering.getDescription(),
                allDisplaySections
        );
    }

    /**
     * Maps one Section entity into a stream of DisplaySectionDetails.
     * @param section is the Section entity we are looking for details about.
     */
    private Stream<DisplaySectionDetails> mapSectionToDisplayDetails(Section section) {

        final String sectionName = section.getSectionName();

        // Check for null professor name to prevent crashing
        final String professorName = getProfessorNameBySectionId(sectionName);

        // Create the placeholder professor DTO
        final DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                professorName != null ? professorName : "TBD", 0.0, 0.0, null
        );

        // Return a stream containing a single DTO for this section.
        return Stream.of(new DisplaySectionDetails(
                sectionName,
                placeholderProf
        ));
    }

    @Override
    public String getProfessorNameBySectionId(String sectionId) {
        // This remains the same, delegating the lookup to the repository
        return courseRepository.getProfessorNameBySectionId(sectionId);
    }
}