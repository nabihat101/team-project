package interface_adapter.login;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Login View.
 * It holds the state of the view and notifies listeners when the state changes.
 */
public class LoginViewModel {

    private final List<LoginViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "log in";

    private LoginState state = new LoginState();

    /**
     * @return the current state of the Login View Model
     */
    public LoginState getState() {
        return this.state;
    }

    /**
     * Sets the state of the Login View Model.
     * @param state the new state to set
     */
    public void setState(LoginState state) {
        this.state = state;
    }

    /**
     * Adds a listener to the Login View Model.
     * @param listener the listener to add
     */
    public void addListener(LoginViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state of the Login View Model has changed.
     */
    public void notifyStateChanged() {
        for (LoginViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}