package use_case.osrm_walktime;

public class WalkTimeInputData {
    private final String startCode;
    private final String endCode;

    public WalkTimeInputData(String startCode, String endCode) {
        this.startCode = startCode;
        this.endCode = endCode;
    }

    public String getStartCode() { return startCode; }
    public String getEndCode() { return endCode; }
}