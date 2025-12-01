package data_access.course_data;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonCourseDataRepository implements CourseDataRepository, CourseDataRepositoryGrouped {
    private final Map<String, Map<String, CourseOffering>> CourseInfobyCode;
    private final Map<String, CourseOffering> availableCourseOfferings;
    private final Map<String, String> sectionIdToProfessorName = new HashMap<>();

    public JsonCourseDataRepository(List<String> dataResourceNames) {
        long start = System.currentTimeMillis();
        AtomicInteger numFilesLoaded = new AtomicInteger();

        CourseInfobyCode = new HashMap<>();
        availableCourseOfferings = new HashMap<>();

        dataResourceNames.forEach(resourceName -> {
            URL resource = this.getClass().getClassLoader().getResource(resourceName);
            if (resource == null) {
                System.err.println(
                        "Specified course data resource file named `" + resourceName + "` not found. Skipping.");
                return;
            }

            numFilesLoaded.getAndIncrement();
            long curr = System.currentTimeMillis();
            long elapsed = curr - start;
            System.out.println("[" + this + "] loaded " + numFilesLoaded + " files at " + elapsed + "ms");
            String coursecode = resourceName.replace("courses/", "").replace(".json", "").toUpperCase();

            Map<String, CourseOffering> currentavailableCourseOfferings = loadInCoursesFromJsonFile(resource);

            if (currentavailableCourseOfferings != null) {
                CourseInfobyCode.put(coursecode, currentavailableCourseOfferings);
                availableCourseOfferings.putAll(currentavailableCourseOfferings);
            }

        });
    }

    private Map<String, CourseOffering> loadInCoursesFromJsonFile(URL resource) {
        String contents;
        try {
            contents = Files.readString(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            return null;
        }

        Map<String, CourseOffering> currentavailableCourseOfferings = new HashMap<>();

        JSONObject object = new JSONObject(contents);
        object.keys().forEachRemaining(courseOfferingIdentifier -> {
            JSONObject currOfferingObj = object.getJSONObject(courseOfferingIdentifier);

            String courseCodeString = currOfferingObj.getString("code");
            String title = currOfferingObj.getString("courseTitle");
            String description = currOfferingObj.getString("courseDescription");

            CourseOffering courseOffering;

            try {
                courseOffering = new CourseOffering(
                        courseOfferingIdentifier,
                        new CourseCode(courseCodeString),
                        title,
                        description);
            } catch (IllegalArgumentException e) {
                System.err.println("Could not load course with identifier " +
                        courseOfferingIdentifier + " because " + e.getMessage());
                return;
            }

            //  START OF SECTION/MEETING PROCESSING
            JSONObject sectionsObj = currOfferingObj.getJSONObject("meetings");

            // Loop over each section (e.g., "LEC-0101", "TUT-0102")
            sectionsObj.keys().forEachRemaining(sectionId -> {

                final JSONObject sectionDetails = sectionsObj.getJSONObject(sectionId);

                // EXTRACT PROFESSOR NAME
                String professorName = "TBD Professor";

                if (sectionDetails.has("instructors") && !sectionDetails.isNull("instructors")) {
                    final JSONObject instructorsObj = sectionDetails.getJSONObject("instructors");
                    // Check if the primary instructor (key "0") exists
                    if (instructorsObj.has("0") && !instructorsObj.isNull("0")) {
                        final JSONObject primaryInstructor = instructorsObj.getJSONObject("0");
                        final String firstName = primaryInstructor.getString("firstName");
                        final String lastName = primaryInstructor.getString("lastName");
                        professorName = firstName + " " + lastName;
                    }
                }

                // STORE MAPPING INTERNALLY for future lookup
                String compositeKey = courseCodeString + ":" + sectionId;
                sectionIdToProfessorName.put(compositeKey, professorName);
                // System.out.println("Mapping professor for section ID: " + sectionId + " -> " + professorName);
                Section section = new Section(courseOffering, sectionId, Section.TeachingMethod.LECTURE);

                JSONObject scheduleObj = sectionDetails.getJSONObject("schedule");

                for (String scheduleEntryKey : scheduleObj.keySet()) {

                    JSONObject scheduleEntry = scheduleObj.getJSONObject(scheduleEntryKey);

                    // Extract meeting day string, e.g. "MO", "WE"
                    String meetingDay = scheduleEntry.optString("meetingDay", null);
                    String startTimeStr = scheduleEntry.optString("meetingStartTime", null);
                    String endTimeStr = scheduleEntry.optString("meetingEndTime", null);
                    String building = scheduleEntry.optString("assignedRoom1", "");

                    // vic - i dont know where the rooms are in the data but add it here if u find its key
                    // String room = scheduleEntry.optString("assignedRoom2", "");

                    if (meetingDay == null || startTimeStr == null || endTimeStr == null) {
                        continue; // Skip incomplete meeting info
                    }

                    // Map the short day string to the DayOfTheWeek enum explicitly
                    WeeklyOccupancy.DayOfTheWeek dayEnum = mapDayCode(meetingDay);
                    if (dayEnum == null) {
                        continue;  // Skip if no valid day mapping found
                    }

                    // Convert "HH:mm" to milliseconds
                    int startMs = convertTimeStringToMilliseconds(startTimeStr);
                    int endMs = convertTimeStringToMilliseconds(endTimeStr);

                    // Create WeeklyOccupancy for this timespan
                    WeeklyOccupancy occupancy = new WeeklyOccupancy(dayEnum, startMs, endMs);

                    // Create Location
                    UofTLocation location = new UofTLocation(building, "");

                    // Create the Meeting (semester can be determined or hardcoded)
                    Meeting meeting = new Meeting(location, Meeting.Semester.FIRST, occupancy);

                    section.addMeeting(meeting);
                }

                // add the section and courseOffering
                courseOffering.addAvailableSection(section);
                availableCourseOfferings.put(courseOfferingIdentifier, courseOffering);

            });
            currentavailableCourseOfferings.put(courseOfferingIdentifier, courseOffering);
        });

        return currentavailableCourseOfferings;
    }

    private WeeklyOccupancy.DayOfTheWeek mapDayCode(String code) {
        switch (code) {
            case "MO": return WeeklyOccupancy.DayOfTheWeek.MONDAY;
            case "TU": return WeeklyOccupancy.DayOfTheWeek.TUESDAY;
            case "WE": return WeeklyOccupancy.DayOfTheWeek.WEDNESDAY;
            case "TH": return WeeklyOccupancy.DayOfTheWeek.THURSDAY;
            case "FR": return WeeklyOccupancy.DayOfTheWeek.FRIDAY;
            case "SA": return WeeklyOccupancy.DayOfTheWeek.SATURDAY;
            case "SU": return WeeklyOccupancy.DayOfTheWeek.SUNDAY;
            default:   return null;
        }
    }

    private int convertTimeStringToMilliseconds(String time) {
        // time expected in format "HH:mm"
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }

    @Override
    public CourseOffering getCourseOffering(String courseOfferingIdentifier) {
        return availableCourseOfferings.get(courseOfferingIdentifier);
    }

    // @Override
    // public Set<String> getAllCourseOfferingbycode(String courseCode) {
    //     return availableCourseOfferings.get(courseCode);
    // }

    @Override
    public Map<String, CourseOffering> getMatchingCourseInfo(String deptCode) {
        return CourseInfobyCode.get(deptCode);
    }

    /**
     * Get the professor's name for a course and section.
     *
     * @param courseId  The full course offer identifier (e.g., "CSC108F").
     * @param sectionId The section ID (e.g., "LEC-0101").
     * @return The professor's name, or "TBD Professor" if not found.
     */
    public String getProfessorNameByCourseAndSection(String courseId, String sectionId) {
        String compositeKey = courseId + ":" + sectionId;
        return sectionIdToProfessorName.getOrDefault(compositeKey, "TBD Professor");
    }
}
