package interface_adapter.walktime;

public class WalkTimeState {
    private String walkTimeDisplay = ""; // e.g. "Walk: 6 mins"
    private String error = null;

    public WalkTimeState() {}

    public String getWalkTimeDisplay() {
        return walkTimeDisplay;
    }

    public void setWalkTimeDisplay(String walkTimeDisplay) {
        this.walkTimeDisplay = walkTimeDisplay;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}