package data_access.display_course_context;

import data_access.course_data.CourseDataRepository;
import entity.CourseOffering;
import entity.Section;
import entity.WeeklyOccupancy;
import use_case.display_course_context.*;
import data_access.course_data.JsonCourseDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<String, CourseOffering> deptOfferings =
                courseRepository.getMatchingCourseInfo(deptCode);

        if (deptOfferings == null || deptOfferings.isEmpty()) {
            return null; // Department not found or no offerings
        }

        // 3. Filter offerings to find all versions of the requested course (e.g., ABP102Y1F, ABP102Y1S)
        List<CourseOffering> matchingOfferings = deptOfferings.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(courseId))
                .map(Map.Entry::getValue)
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

        final List<DisplayMeetingTime> meetingTimes = section.getMeetingsCopy()
                .stream()
                .map(meeting -> {
                    WeeklyOccupancy occ = meeting.getTime();

                    int dayIndex = occ.getDayOfTheWeek();    // 0..6
                    int startMs  = occ.getStartTimeInDay();  // millis within day
                    int endMs    = occ.getEndTimeInDay();    // millis within day

                    if (dayIndex < 0 || startMs < 0 || endMs < 0) {
                        return null; // skip weird/empty data
                    }

                    String dayOfWeek = WeeklyOccupancy.DayOfTheWeek
                            .values()[dayIndex]
                            .name()
                            .substring(0, 3);   // "MON", "TUE", ...

                    String start = formatTimeOfDay(startMs);
                    String end   = formatTimeOfDay(endMs);

                    return new DisplayMeetingTime(dayOfWeek, start, end);
                })
                .filter(mt -> mt != null)
                .collect(Collectors.toList());

        // Location: pick from first meeting (or set "TBA" if none)
        String location = section.getMeetingsCopy().stream()
                .findFirst()
                .map(meeting -> meeting.getLocation().toString())  // adjust to your actual getter
                .orElse("TBA");

        // Professor info
        final String professorName = getProfessorNameBySectionId(sectionName);
        final DisplayProfessorDetails placeholderProf = new DisplayProfessorDetails(
                professorName != null ? professorName : "TBD",
                0.0,
                0.0,
                null
        );

        return Stream.of(new DisplaySectionDetails(
                sectionName,
                meetingTimes,
                location,
                placeholderProf
        ));
    }

    private static String formatTimeOfDay(int millisInDay) {
        if (millisInDay < 0) return "?";
        int totalMinutes = millisInDay / (1000 * 60);
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    @Override
    public String getProfessorNameBySectionId(String sectionId) {
        // This remains the same, delegating the lookup to the repository
        return courseRepository.getProfessorNameBySectionId(sectionId);
    }
}