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
