package interface_adapter.walktime;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class WalkTimeViewModel {

    private static final String PROPERTY_STATE = "walk_time_state";
    private WalkTimeState state = new WalkTimeState();

    public WalkTimeViewModel() {
        super();
    }

    public void setState(WalkTimeState state) {
        this.state = state;
    }

    public WalkTimeState getState() {
        return state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Alert the View that something changed
    public void firePropertyChanged() {
        support.firePropertyChange(PROPERTY_STATE, null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}