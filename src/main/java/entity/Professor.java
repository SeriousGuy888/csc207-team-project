package entity;

/**
 * Represents a Professor
 * <p>
 * An instance of this class will contain information such as the professor's first and last name,
 * RateMyProf Rating, and AvgDifficulty rating.
 */
public class Professor {
    private final String firstName;
    private final String lastName;
    private final float avgRating;
    private final int numRatings;
    private final float avgDifficultyRating;

    public Professor(String firstName, String lastName, float avgRating, int numRatings, float avgDifficultyRating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
        this.avgDifficultyRating = avgDifficultyRating;
    }
}
