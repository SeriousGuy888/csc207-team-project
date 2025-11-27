package interface_adapter.walktime;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class WalkTimeViewModel {

    private WalkTimeState state = new WalkTimeState();

    public WalkTimeViewModel() {}

    public void setState(WalkTimeState state) {
        this.state = state;
    }

    public WalkTimeState getState() {
        return state;
    }

    // Standard PropertyChange logic (Boilerplate for Swing)
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}