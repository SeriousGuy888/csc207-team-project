package use_case;

/**
 * Input Data packet for the Walk Time Use Case.
 */
public class WalkTimeInputData {

    final private String originCode;
    final private String destinationCode;

    public WalkTimeInputData(String originCode, String destinationCode) {
        this.originCode = originCode;
        this.destinationCode = destinationCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }
}