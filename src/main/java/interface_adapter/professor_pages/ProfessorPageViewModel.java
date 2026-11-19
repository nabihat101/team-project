package interface_adapter.professor_pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores current state and notifies view when state changes
 */
public class ProfessorPageViewModel {
    private ProfessorPageState state = new ProfessorPageState();
    public static String VIEW_NAME = "professor page";
    private List<ProfessorPageViewModelListener> listeners = new ArrayList<>();

    /**
     * Return current state
     * @return state - ProfessorPageState
     */
    public ProfessorPageState getState(){
        return this.state;
    }

    /**
     * Set state to new state
     * @param state - ProfessorPageState
     */
    public void setState(ProfessorPageState state) {
        this.state = state;
    }

    /**
     * Add listener to list to be notified when state changed
     * @param listener - ProfessorPageViewModelListener
     */
    public void addListener(ProfessorPageViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notified listeners when state changes
     */
    public void notifyStateChanged() {
        for (ProfessorPageViewModelListener listener: listeners) {
            listener.stateChanged();
        }
    }
}
