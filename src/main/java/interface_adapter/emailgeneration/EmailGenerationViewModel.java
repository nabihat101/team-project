package interface_adapter.emailgeneration;

import java.util.ArrayList;
import java.util.List;

public class EmailGenerationViewModel {
    private final List<EmailGenerationViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "email generation";

    private EmailGenerationState state = new EmailGenerationState();

    /**
     * @return the current state of the Email Generation View Model
     */
    public EmailGenerationState getState() {
        return this.state;
    }

    /**
     * Sets the state of the Email Generation View Model
     * @param state the new state to set
     */
    public void setState(EmailGenerationState state) {
        this.state = state;
    }

    /**
     * Adds a listener to the Email Generation View Model
     * @param listener the listener to add
     */
    public void addListener(EmailGenerationViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state of the Email Generation View Model has changed.
     */
    public void notifyStateChanged() {
        for (EmailGenerationViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
