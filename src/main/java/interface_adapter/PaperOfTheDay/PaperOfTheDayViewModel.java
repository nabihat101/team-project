package interface_adapter.PaperOfTheDay;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Paper of the Day View.
 * It holds the state of the view and notifies listeners when the state changes.
 */
public class PaperOfTheDayViewModel {

    private final List<PaperOfTheDayViewModelListener> listeners = new ArrayList<>();
    public static final String VIEW_NAME = "paper of the day";
    private PaperOfTheDayState state = new PaperOfTheDayState();

    /**
     * @return the current state of the Paper of the Day View Model
     */
    public PaperOfTheDayState getState() {
        return this.state;
    }

    /**
     * Sets the state of the Paper of the Day View Model.
     * @param state the new state to set
     */
    public void setState(PaperOfTheDayState state) {
        this.state = state;
    }

    /**
     * Adds a listener to the Paper of the Day View Model.
     * @param listener the listener to add
     */
    public void addListener(PaperOfTheDayViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state of the Paper of the Day View Model has changed.
     */
    public void notifyStateChanged() {
        for (PaperOfTheDayViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}