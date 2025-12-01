package use_case.display_course_context;

/**
 * Data transfer object for professor details
 */
public class DisplayProfessorDetails {
    private final String name;
    private final double avgRating;
    private final double avgDifficultyRating;
    private final String link;

    public DisplayProfessorDetails(String name, double avgRating, double avgDifficultyRating, String link) {
        this.name = name;
        this.avgRating = avgRating;
        this.avgDifficultyRating = avgDifficultyRating;
        this.link = link;
    }

    // Getters
    public String getName() { return name; }
    public double getAvgRating() { return avgRating; }
    public double getAvgDifficultyRating() { return avgDifficultyRating; }
    public String getLink() { return link; }
}
