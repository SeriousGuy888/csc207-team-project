package data_access.workbook_persistence;

import com.google.gson.*;
import entity.CourseCode;
import entity.CourseOffering;
import entity.Workbook;
import use_case.load_workbook.LoadWorkbookDataAccessInterface;
import use_case.save_workbook.SaveWorkbookDataAccessInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWorkbookDataAccessObject implements SaveWorkbookDataAccessInterface, LoadWorkbookDataAccessInterface {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(CourseOffering.class, new CourseOfferingJsonSerializerAndDeserializer())
            .create();

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


    static class CourseOfferingJsonSerializerAndDeserializer
            implements JsonSerializer<CourseOffering>, JsonDeserializer<CourseOffering> {
        @Override
        public JsonElement serialize(CourseOffering courseOffering,
                                     Type type,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(courseOffering.getUniqueIdentifier());
        }

        @Override
        public CourseOffering deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            String courseIdentifierString = jsonElement.getAsJsonPrimitive().getAsString();

            // todo: load the course from a real data access object
            //  that actually a list of all the courses

            return new CourseOffering(courseIdentifierString,
                    new CourseCode(courseIdentifierString.substring(0, 8)), "test", "ing");
        }
    }
}
