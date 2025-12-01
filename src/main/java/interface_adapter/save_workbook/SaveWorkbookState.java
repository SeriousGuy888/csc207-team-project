package interface_adapter.save_workbook;

public class SaveWorkbookState {
    private final boolean success;
    private final String message;

    public SaveWorkbookState(boolean success, String message) {
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
