package use_case.ratemyprof;

/**
 * Output Data for the RateMyProf Use Case.
 */
public class RateMyProfOutputData {

    private final String profFirstName;
    private final String profLastName;

    public RateMyProfOutputData(String profFirstName, String profLastName) {
        this.profFirstName = profFirstName;
        this.profLastName = profLastName;
    }

    public String getprofFirstName() {
        return this.profFirstName;
    }
    public String getprofLastName() {
        return this.profLastName;
    }
}

