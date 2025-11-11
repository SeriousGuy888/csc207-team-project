package auto;

public class LockedKey {
    private String courseCode;
    private String sectionCode;

    public LockedKey(String courseCode, String sectionCode) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
    }

    public String getCourseCode() { return courseCode; }
    public String getSectionCode() { return sectionCode; }

}
