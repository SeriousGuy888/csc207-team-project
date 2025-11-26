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

public class JsonCourseDataRepository implements CourseDataRepository, CourseDataRepository_CourseCode {
    private final Map<String, Map<String, CourseOffering>> CourseInfobyCode;
    private final Map<String, CourseOffering> availableCourseOfferings;

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

            CourseInfobyCode.put(coursecode, currentavailableCourseOfferings);
            availableCourseOfferings.putAll(currentavailableCourseOfferings);
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

            JSONObject sectionsObj = currOfferingObj.getJSONObject("meetings");
            sectionsObj.keys().forEachRemaining(sectionName -> {
                // todo: actually choose the right teaching method
                //  and also add meeting times
                Section section = new Section(courseOffering, sectionName, Section.TeachingMethod.LECTURE);

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
    public Map<String, CourseOffering> getMatchingCourseInfo(String courseCode) {
        return CourseInfobyCode.get(courseCode);
    }
}
