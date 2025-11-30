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
import java.util.concurrent.atomic.AtomicInteger;

public class JsonCourseDataRepository implements CourseDataRepository {
    private final Map<String, CourseOffering> availableCourseOfferings;

    public JsonCourseDataRepository(List<String> dataResourceNames) {
        long start = System.currentTimeMillis();
        AtomicInteger numFilesLoaded = new AtomicInteger();

        availableCourseOfferings = new HashMap<>();
        dataResourceNames.forEach(resourceName -> {
            URL resource = this.getClass().getClassLoader().getResource(resourceName);
            if (resource == null) {
                System.err.println(
                        "Specified course data resource file named `" + resourceName + "` not found. Skipping.");
                return;
            }
            loadInCoursesFromJsonFile(resource);

            numFilesLoaded.getAndIncrement();
            long curr = System.currentTimeMillis();
            long elapsed = curr - start;
            System.out.println("[" + this + "] loaded " + numFilesLoaded + " files at " + elapsed + "ms");
        });
    }

    private void loadInCoursesFromJsonFile(URL resource) {
        String contents;
        try {
            contents = Files.readString(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            return;
        }

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

            JSONObject sectionsObj = currOfferingObj.getJSONObject("meetings");
            sectionsObj.keys().forEachRemaining(sectionName -> {
                // todo: actually choose the right teaching method
                //  and also add meeting times
                Section section = new Section(courseOffering, sectionName, Section.TeachingMethod.LECTURE);

                courseOffering.addAvailableSection(section);
            });

            availableCourseOfferings.put(courseOfferingIdentifier, courseOffering);
        });
    }

    @Override
    public CourseOffering getCourseOffering(String courseOfferingIdentifier) {
        return availableCourseOfferings.get(courseOfferingIdentifier);
    }
}
