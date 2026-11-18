package interface_adapter.loggedin;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel {

    private final List<LoggedInViewModelListener> listeners = new ArrayList<>();
    public static final String VIEW_NAME = "logged in";
    private LoggedInState state = new LoggedInState();

    /**
     * Gets the current state of the Logged In View.
     *
     * @return the current LoggedInState
     */
    public LoggedInState getState() {
        return this.state;
    }

    /**
     * Sets the current state of the Logged In View.
     *
     * @param state the new LoggedInState
     */
    public void setState(LoggedInState state) {
        this.state = state;
    }

    /**
     * Adds a listener to the Logged In View Model.
     *
     * @param listener the listener to add
     */
    public void addListener(LoggedInViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state of the Logged In View Model has changed.
     */
    public void notifyStateChanged() {
        for (LoggedInViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    /**
     * Notifies all listeners that the password in the Logged In View Model has changed.
     */
    public void notifyPasswordChangeUpdate() {
        for (LoggedInViewModelListener listener : listeners) {
            listener.passwordChanged();
        }
    }
}