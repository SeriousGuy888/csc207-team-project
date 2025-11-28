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


    public String getProfFirstName() {
        return this.profFirstName;
    }

    public String getProfLastName() {
        return this.profLastName;
    }

    public double getAvgRating() {
        return this.avgRating;
    }

    public int getNumRatings() {
        return this.numRatings;
    }

    public double getAvgDifficultyRating() {
        return this.avgDifficultyRating;
    }

    public String getLink(){
        return this.link;
    }
}

