package use_case;

public class WalkTimeOutputData {

    private final int walkTimeInMinutes;
    private final boolean useCaseFailed;

    public WalkTimeOutputData(int walkTimeInMinutes, boolean useCaseFailed) {
        this.walkTimeInMinutes = walkTimeInMinutes;
        this.useCaseFailed = useCaseFailed;
    }

    public int getWalkTimeInMinutes() {
        return walkTimeInMinutes;
    }
}
