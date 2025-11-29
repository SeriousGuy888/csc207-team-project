package use_case.osrm_walktime;

public class WalkTimeOutputData {
    private final int durationInSeconds;

    public WalkTimeOutputData(int durationInSeconds, boolean useCaseFailed) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getDurationInSeconds() { return durationInSeconds; }
}