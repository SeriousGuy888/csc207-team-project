package interface_adapter.search_courses;

import interface_adapter.ViewModel;

public class SearchCoursesViewModel extends ViewModel<SearchCoursesState> {
    public static final String SEARCH_RESULTS_UPDATED = "searchResultsUpdated";

    public SearchCoursesViewModel() {
        super("search_courses");
    }
}