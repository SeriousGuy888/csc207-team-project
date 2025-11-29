package use_case.add_section;

public class AddSectionInputData {
    private final String courseOfferingAsString;    // ex. "CSC207H1" (from UI - Search Panel)
    private final String sectionName;               // ex. "LEC0101" (from UI - Expanded Accordion Panel)
    private final int selectedTabIndex;               // from GlobalViewState.getSelectedTabIndex()

    public AddSectionInputData(String courseOfferingAsString, String sectionName, int selectedTabIndex) {
        this.courseOfferingAsString = courseOfferingAsString;
        this.sectionName = sectionName;
        this.selectedTabIndex = selectedTabIndex;
    }

    public String getCourseOfferingAsString() {
        return courseOfferingAsString;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
}