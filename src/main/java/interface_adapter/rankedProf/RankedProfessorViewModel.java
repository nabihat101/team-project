package interface_adapter.rankedProf;

import java.util.ArrayList;
import java.util.List;

/** Stores the current state and notifies the view when it changes. */
public class RankedProfessorViewModel {

    private final List<RankedProfessorViewModelListener> listeners = new ArrayList<>();
    public static final String VIEW_NAME = "ranked professors";
    private RankedProfessorState state = new RankedProfessorState();

    /**
     * @return the current state of the Ranked Professor View Model
     */
    public RankedProfessorState getState() {
        return state;
    }


    /**
     * Sets the state of the Ranked Professor View Model.
     * @param state the new state to set
     */
    public void setState(RankedProfessorState state) {
        this.state = state;
    }


    /**
     * Adds a listener to the Ranked View Model.
     * @param listener the listener to add
     */
    public void addListener(RankedProfessorViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the state of the Ranked Professor View Model has changed.
     */
    public void notifyStateChanged() {
        for (RankedProfessorViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}