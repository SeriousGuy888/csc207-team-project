package interface_adapter.autogen;

import use_case.WorkbookDataAccessInterface;
import use_case.autogen.AutogenInputBoundary;
import use_case.autogen.AutogenInputData;

import java.util.Set;

public class AutogenController {

    private final AutogenInputBoundary interactor;
    private final WorkbookDataAccessInterface workbookDao;

    public AutogenController(AutogenInputBoundary interactor,
                             WorkbookDataAccessInterface workbookDao) {
        this.interactor = interactor;
        this.workbookDao = workbookDao;
    }

    public void autogenerate() {
        // Pull info from workbook/current tab to form AutogenInputData.
        // For now, you could even hardcode or do Set.of() placeholders,
        // and refine later based on how you store selections.

        // Example placeholder:
        AutogenInputData input = new AutogenInputData(
                Set.of(),
                Set.of(),
                null
        );

        interactor.execute(input);
    }
}

