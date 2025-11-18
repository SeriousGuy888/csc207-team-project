package data_access.workbook_persistence.strategies;

import com.google.gson.*;
import entity.CourseCode;
import entity.CourseOffering;
import entity.Workbook;

import java.lang.reflect.Type;
import java.text.ParseException;

public class GsonWorkbookSerialiser extends WorkbookSerialiser {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(CourseOffering.class, new CourseOfferingJsonSerializerAndDeserializer())
            .create();

    @Override
    public String serialise(Workbook workbook) {
        return gson.toJson(workbook);
    }

    @Override
    public Workbook deserialise(String serialisedWorkbook) throws ParseException {
        try {
            return gson.fromJson(serialisedWorkbook, Workbook.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static class CourseOfferingJsonSerializerAndDeserializer
            implements JsonSerializer<CourseOffering>, JsonDeserializer<CourseOffering> {
        @Override
        public JsonElement serialize(CourseOffering courseOffering,
                                     Type type,
                                     JsonSerializationContext context) {
            // todo: make this into a unique identifier rather than just the course code
            //  (the same course can be taught twice in one session)
            return new JsonPrimitive(courseOffering.getUniqueIdentifier());
        }

        @Override
        public CourseOffering deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            // todo: make this retrieve the course based on the unique identifier from some data access object
            //  that is storing a list of available course offerings.
            CourseCode courseCode = new CourseCode(jsonElement.getAsJsonPrimitive().getAsString());
            return new CourseOffering("MAT237Y1-F-20259", courseCode, "test", "ing");
        }
    }
}
