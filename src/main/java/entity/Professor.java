package entity;

/**
 * Represents a Professor
 * <p>
 * An instance of this class will contain information such as the professor's first and last name,
 * RateMyProf Rating, and AvgDifficulty rating.
 */
public class Professor {
    private String firstName;
    private String lastName;
    private double avgRating;
    private int numRatings;
    private double avgDifficultyRating;
    private String department;
    private String link;

    public Professor(String firstName, String lastName, double avgRating, int numRatings, double avgDifficultyRating, String department, String link) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
        this.avgDifficultyRating = avgDifficultyRating;
        this.department = department;
        this.link = link;
    }

    /**
    * @return a formatted string with the professor info
    */
    @Override
    public String toString() {
        return "first: " + this.firstName + " | last: " + this.lastName + " | avgRating: " + this.avgRating +
                " | numRatings: " + this.numRatings + " | avgDifficulty: " + this.avgDifficultyRating +
                " | department: " + this.department + " | link: " + this.link;
    }

   /**
    * @return an empty professor
    */
    public static Professor emptyProfessor() {
        return new Professor("", "", 0.0, 0, 0.0, "", "");
    }

}
