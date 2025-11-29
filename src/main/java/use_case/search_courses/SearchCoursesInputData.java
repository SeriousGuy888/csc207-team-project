package use_case.search_courses;

public class SearchCoursesInputData {
    public final String query;

    public SearchCoursesInputData(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}