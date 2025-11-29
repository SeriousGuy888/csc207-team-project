package data_access.course_data;

import entity.CourseCode;
import entity.CourseOffering;
import entity.Section;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonCourseDataRepository implements CourseDataRepository, CourseDataRepositoryGrouped {
    private final Map<String, Map<String, CourseOffering>> CourseInfobyCode;
    private final Map<String, CourseOffering> availableCourseOfferings;
    private final Map<String, String> sectionIdToProfessorName = new HashMap<>();

    public JsonCourseDataRepository(List<String> dataResourceNames) {
        CourseInfobyCode = new HashMap<>();
        availableCourseOfferings = new HashMap<>();

        dataResourceNames.forEach(resourceName -> {
            URL resource = this.getClass().getClassLoader().getResource(resourceName);
            if (resource == null) {
                System.err.println(
                        "Specified course data resource file named `" + resourceName + "` not found. Skipping.");
                return;
            }

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

            CourseOffering courseOffering = new CourseOffering(
                    courseOfferingIdentifier,
                    new CourseCode(courseCodeString),
                    title,
                    description);

            //  START OF SECTION/MEETING PROCESSING
            JSONObject sectionsObj = currOfferingObj.getJSONObject("meetings");

            // Loop over each section (e.g., "LEC-0101", "TUT-0102")
            sectionsObj.keys().forEachRemaining(sectionId -> {

                JSONObject sectionDetails = sectionsObj.getJSONObject(sectionId);

                // EXTRACT PROFESSOR NAME
                String professorName = "TBD Professor";

                if (sectionDetails.has("instructors") && !sectionDetails.isNull("instructors")) {
                    JSONObject instructorsObj = sectionDetails.getJSONObject("instructors");
                    // Check if the primary instructor (key "0") exists
                    if (instructorsObj.has("0") && !instructorsObj.isNull("0")) {
                        JSONObject primaryInstructor = instructorsObj.getJSONObject("0");
                        String firstName = primaryInstructor.getString("firstName");
                        String lastName = primaryInstructor.getString("lastName");
                        professorName = firstName + " " + lastName;
                    }
                }

                // STORE MAPPING INTERNALLY for future lookup
                sectionIdToProfessorName.put(sectionId, professorName);

                // todo: actually choose the right teaching method
                //  and also add meeting times
                Section section = new Section(courseOffering, sectionId, Section.TeachingMethod.LECTURE);

                courseOffering.addAvailableSection(section);

            });
            currentavailableCourseOfferings.put(courseOfferingIdentifier, courseOffering);
            });

        return currentavailableCourseOfferings;
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

    public String getProfessorNameBySectionId(String sectionId) {
        // Uses the map populated in loadInCoursesFromJsonFile
        return sectionIdToProfessorName.getOrDefault(sectionId, "TBD Professor");
    }



    }
}
