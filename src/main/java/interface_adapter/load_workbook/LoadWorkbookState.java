package interface_adapter.load_workbook;

public class LoadWorkbookState {
    private final boolean success;
    private final String message;

    public LoadWorkbookState(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
