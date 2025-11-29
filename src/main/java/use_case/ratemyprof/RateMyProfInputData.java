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

    String getFirstName() {
        return profFirstName;
    }

    String getLastName() {
        return profLastName;
    }
}
