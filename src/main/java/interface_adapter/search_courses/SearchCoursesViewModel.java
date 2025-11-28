package interface_adapter.search_courses;

public class SearchCoursesViewModel extends ViewModel<SearchCoursesViewState> {
    public static final String SEARCH_RESULTS_UPDATED = "search_results_updated";

    public SearchCoursesViewModel() {
        super("search_courses_view");
        setState(new SearchCoursesViewState());
    }
}