package interface_adapter.display_course_context;

/**
 * Data for a single row in the sections table/list.
 */
public class SectionDisplayData {
    private final String sectionName; // e.g., LEC0101
    private final String timeLocation; // e.g., MON, WED 10:00-12:00 in BA 1100
    private final String professorName; // e.g., Paul Gries
    private final String avgRating;       // e.g., 4.4
    private final String avgDifficulty; // e.g., 2.7
    private final String rmpLink; // The direct link to RateMyProf

    public SectionDisplayData(String sectionName, String timeLocation, String professorName,
                              String avgRating, String avgDifficulty, String rmpLink) {
        this.sectionName = sectionName;
        this.timeLocation = timeLocation;
        this.professorName = professorName;
        this.avgRating = avgRating;
        this.avgDifficulty = avgDifficulty;
        this.rmpLink = rmpLink;
    }

    // Getters
    public String getSectionName() { return sectionName; }
    public String getTimeLocation() { return timeLocation; }
    public String getProfessorName() { return professorName; }
    public String getAvgRating() { return avgRating; }
    public String getAvgDifficulty() { return avgDifficulty; }
    public String getRmpLink() { return rmpLink; }
}