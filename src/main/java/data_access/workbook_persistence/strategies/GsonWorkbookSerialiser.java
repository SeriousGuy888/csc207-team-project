package data_access.workbook_persistence.strategies;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entity.Workbook;

public class GsonWorkbookSerialiser extends WorkbookSerialiser {
    private final Gson gson = new Gson();

    @Override
    public String serialise(Workbook workbook) {
        return gson.toJson(workbook);
    }

    @Override
    public Workbook deserialise(String serialisedWorkbook) throws IllegalArgumentException {
        try {
            return gson.fromJson(serialisedWorkbook, Workbook.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
