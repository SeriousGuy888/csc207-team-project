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

    public String getViewName() {
        return this.viewName;
    }

    public T getState() {
        return state;
    }

    /**
     * Set the state of the view model
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
     * If you have changed the contents of state
     * (i.e. you've either just called {@link #setState(T)} or modified the {@link T} state object),
     * then you should call this method so that listeners are actually notifiers.
     *
     * @param propertyName the name of the property that changed
     */
    public void firePropertyChange(String propertyName) {
        support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific property.
     * The listener will be invoked only when a call on firePropertyChange names that specific property.
     * This allows Views to subscribe only to the events they care about, avoiding unnecessary updates
     * for unrelated state changes.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }
}
