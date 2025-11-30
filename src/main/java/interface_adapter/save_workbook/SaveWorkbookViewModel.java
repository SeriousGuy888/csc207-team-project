package interface_adapter.save_workbook;

import interface_adapter.ViewModel;

public class SaveWorkbookViewModel extends ViewModel<SaveWorkbookState> {
    public static final String STATE_PROPERTY_NAME = "state";

    public SaveWorkbookViewModel() {
        super("save workbook");
    }
}
