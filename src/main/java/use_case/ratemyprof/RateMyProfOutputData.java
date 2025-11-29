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

    /**
     * Returns the professor's first name.
     *
     * @return the first name of this professor
     */
    public String getprofFirstName() {
        return this.profFirstName;
    }

    /**
     * Returns the professor's last name.
     *
     * @return the last name of this professor
     */
    public String getprofLastName() {
        return this.profLastName;
    }
}

