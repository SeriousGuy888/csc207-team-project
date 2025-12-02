package use_case.remove_section;

public class RemoveSectionInputData {
    private final String courseDisplayString;    // ex. "CSC207H1-F" (from UI - Search Panel)
    private final String sectionName;               // ex. "LEC0101" (from UI - Expanded Accordion Panel)
    private final int selectedTabIndex;               // from GlobalViewState.getSelectedTabIndex()

    public RemoveSectionInputData(String courseDisplayString, String sectionName, int selectedTabIndex) {
        this.courseDisplayString = courseDisplayString;
        this.sectionName = sectionName;
        this.selectedTabIndex = selectedTabIndex;
    }

    public String getCourseDisplayString() {
        return courseDisplayString;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
}