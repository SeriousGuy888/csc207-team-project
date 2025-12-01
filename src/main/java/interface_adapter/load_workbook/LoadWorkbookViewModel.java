package interface_adapter.load_workbook;

import interface_adapter.ViewModel;

public class LoadWorkbookViewModel extends ViewModel<LoadWorkbookState> {
    public static final String STATE_PROPERTY_NAME = "state";

    public LoadWorkbookViewModel() {
        super("load workbook");
    }
}
