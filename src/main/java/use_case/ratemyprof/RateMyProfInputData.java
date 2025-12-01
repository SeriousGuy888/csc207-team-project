package use_case.ratemyprof;

/**
 * The input data for the RateMyProf Use Case.
 */
public class RateMyProfInputData {
    private final String profFirstName;
    private final String profLastName;

    public RateMyProfInputData(String profFirstName, String profLastName) {
        this.profFirstName = profFirstName;
        this.profLastName = profLastName;
    }

    public String getFirstName() {
        return profFirstName;
    }
    public String getLastName() {
        return profLastName;
    }
}
