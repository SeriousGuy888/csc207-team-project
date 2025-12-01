package interface_adapter;

public class GlobalViewModel extends ViewModel<GlobalViewState> {
    public static final String TIMETABLE_CHANGED = "timetable_changed";

    public GlobalViewModel() {
        super("global_view");
        setState(new GlobalViewState());
    }
}
