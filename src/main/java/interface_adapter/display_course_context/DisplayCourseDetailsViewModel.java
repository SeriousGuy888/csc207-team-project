package interface_adapter.display_course_context;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;

public class DisplayCourseDetailsViewModel {

    // String constant for the PropertyChangeEvent
    public static final String STATE_PROPERTY = "state";

    // The current, observable state of the panel
    private DisplayState state = new DisplayState(
            "Loading Course...",
            "",
            Collections.emptyList(),
            null
    );

    // Mechanism to notify the View of changes
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Updates the entire state of the ViewModel and notifies listeners (the View).
     */
    public void setState(DisplayState newState) {
        // Notify listeners *before* changing the state
        support.firePropertyChange(STATE_PROPERTY, this.state, newState);
        this.state = newState;
    }

    public DisplayState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
