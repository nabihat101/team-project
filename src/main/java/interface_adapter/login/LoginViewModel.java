package interface_adapter.login;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel {


    public static final String VIEW_NAME = "log in";

    private final List<LoginViewModelListener> listeners = new ArrayList<>();

    private LoginState state = new LoginState();

    /**
     * Returns the current state of the Login View.
     *
     * @return the current login state
     */
    public LoginState getState() {
        return this.state;
    }

    /**
     * Replaces the current state of the Login View.
     *
     * @param state the new login state
     */
    public void setState(LoginState state) {
        this.state = state;
    }

    /**
     *  Listener to be told when the state changes.
     * @param listener the listener to add
     */
    public void addListener(LoginViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state has changed,
     * so they can read the new state and update themselves.
     */
    public void notifyStateChanged() {
        for (LoginViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}