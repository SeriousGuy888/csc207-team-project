package interface_adapter.display_course_context;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;

public class DisplayCourseDetailsViewModel extends ViewModel<DisplayCourseDetailsState> {

    public static final String DISPLAY_COURSE_CONTEXT = "displayCourseContext";

    // The current, observable state of the panel
    private DisplayCourseDetailsState state = new DisplayCourseDetailsState(
            "",
            "Loading Course...",
            "",
            Collections.emptyList(),
            null
    );

    // Mechanism to notify the View of changes
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public DisplayCourseDetailsViewModel() {
        super("display_course_context");
    }

    @Override
    public void setState(DisplayCourseDetailsState newState) {
        final DisplayCourseDetailsState oldState = this.state;
        this.state = newState;
        support.firePropertyChange(DISPLAY_COURSE_CONTEXT, oldState, newState);
    }

    @Override
    public DisplayCourseDetailsState getState() {
        return state;
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }
}
