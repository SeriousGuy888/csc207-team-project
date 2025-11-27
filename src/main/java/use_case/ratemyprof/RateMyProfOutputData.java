package use_case.ratemyprof;
import entity.Professor;

/**
 * Output Data for the RateMyProf Use Case.
 */
public class RateMyProfOutputData {

    private final String profFirstName;
    private final String profLastName;
    private final double avgRating;
    private final int numRatings;
    private final double avgDifficultyRating;
    private final String link;

    public RateMyProfOutputData(Professor prof) {
        this.profFirstName = prof.getFirstName();
        this.profLastName = prof.getLastName();
        this.avgRating = prof.getAvgRating();
        this.numRatings = prof.getNumRatings();
        this.avgDifficultyRating = prof.getAvgDifficultyRating();
        this.link = prof.getLink();
    }


    public String getprofFirstName() {
        return this.profFirstName;
    }

    public String getprofLastName() {
        return this.profLastName;
    }

    public double getavgRating() {
        return this.avgRating;
    }

    public int getnumRatings() {
        return this.numRatings;
    }

    public double getavgDifficultyRating() {
        return this.avgDifficultyRating;
    }

    public String getLink(){
        return this.link;
    }
}

