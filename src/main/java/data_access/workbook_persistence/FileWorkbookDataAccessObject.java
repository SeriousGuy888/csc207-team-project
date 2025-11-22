package data_access.workbook_persistence;

import com.google.gson.*;
import data_access.course_data.CourseDataRepository;
import entity.CourseCode;
import entity.CourseOffering;
import entity.Section;
import entity.Workbook;
import org.json.JSONObject;
import use_case.load_workbook.LoadWorkbookDataAccessInterface;
import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface, LoadWorkbookDataAccessInterface {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Section.class, new SectionCustomGsonSerializer())
            .create();
    private final CourseDataRepository courseDataRepository;

    public FileWorkbookDataAccessObject(CourseDataRepository courseDataRepository) {
        this.courseDataRepository = courseDataRepository;
    }

    @Override
    public void save(Workbook workbook, Path destination) throws IOException {
        Files.createDirectories(destination.subpath(0, destination.getNameCount() - 1));
        Files.writeString(destination, serialiseWorkbookToJson(workbook));
    }

    @Override
    public Workbook load(Path source) throws IOException {
        String serialisedWorkbook = Files.readString(source);
        Workbook workbook;

        try {
            workbook = deserialiseWorkbookFromJson(serialisedWorkbook);
        } catch (JsonSyntaxException e) {
            throw new IOException("could not parse workbook data");
        }

        return workbook;
    }


    private String serialiseWorkbookToJson(Workbook workbook) {
        return gson.toJson(workbook);
    }

    private Workbook deserialiseWorkbookFromJson(String serialisedWorkbook) throws JsonSyntaxException {
        return gson.fromJson(serialisedWorkbook, Workbook.class);
    }


    /**
     * Overrides GSON's default serialisation/deserialisation behaviour.
     * Whenever GSON runs into a Section instance, it defers to this class to serialise/deserialise it.
     */
    class SectionCustomGsonSerializer implements JsonSerializer<Section>, JsonDeserializer<Section> {
        private final String SECTION_NAME_KEY = "section_name";
        private final String COURSE_OFFERING_KEY = "course_offering";

        @Override
        public JsonElement serialize(Section section,
                                     Type type,
                                     JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add(SECTION_NAME_KEY, new JsonPrimitive(section.getSectionName()));
            result.add(COURSE_OFFERING_KEY, new JsonPrimitive(section.getCourseOffering().getUniqueIdentifier()));
            return result;
        }

        @Override
        public Section deserialize(JsonElement jsonElement,
                                   Type type,
                                   JsonDeserializationContext context)
                throws JsonParseException {

            // attempt to read this json element as a section
            // by trying to read some specific keys from the object
            String sectionName;
            String courseOfferingIdentifier;
            try {
                JsonObject object = jsonElement.getAsJsonObject();
                sectionName = object.get(SECTION_NAME_KEY).getAsString();
                courseOfferingIdentifier = object.get(COURSE_OFFERING_KEY).getAsString();
            } catch (IllegalStateException | // element is not object or contents at keys are not strings
                     UnsupportedOperationException | // contents are nonsingleton arrays
                     NullPointerException e // keys don't exist
            ) {
                throw new JsonParseException("Could not parse section name or course offering.", e);
            }

            // we need a course offering object. pick the one that exists if possible
            CourseOffering courseOffering = courseDataRepository.getCourseOffering(courseOfferingIdentifier);
            if (courseOffering == null) {
                courseOffering = CourseOffering.createUnknownCourseOffering(courseOfferingIdentifier);
            }

            // return the matching section from this course offering if possible
            Optional<Section> matchingSection = courseOffering
                    .getAvailableSections()
                    .stream()
                    .filter(section -> section.getSectionName().equals(sectionName))
                    .findFirst();
            final CourseOffering finalCourseOffering = courseOffering;
            return matchingSection
                    .orElseGet(() -> Section.createUnknownSection(finalCourseOffering, sectionName));
        }
    }
}
