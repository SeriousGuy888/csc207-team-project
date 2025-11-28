package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Manages state that a view (or multiple views) may depend on.
 * Can notify its listeners (i.e. the dependent views) about changes in state.
 *
 * @param <T> type of object that represents the state of this view model
 */
public class ViewModel<T> {
    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() { return this.viewName; }
    public T getState() {
        return state;
    }

    /**
     * set the state of the view model
     * <h2>Important Note</h2>
     * Calling this method does NOT push updates to listeners.
     *
     * @param state the new state
     */
    public void setState(T state) {
        this.state = state;
    }

    /**
     * Pushes a property change event to all listeners of this view model
     * listening for state changes.
     * <p>
     * If you have changed the contents of state
     * (i.e. you've either just called {@link #setState(T)} or modified the {@link T} state object),
     * then you should call this method so that listeners are actually notifiers.
     *
     * @param propertyName the name of the property that changed
     */
    public void fireStateChangeEvent(String propertyName) {
        support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Subscribe the provided {@code listener} to receiving updates every time
     * the state of this view model changes.
     *
     * @param listener the object to subscribe to updates
     */
    public void addStateChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
