package interface_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Current state that is displayed
 */
public class ViewManagerModel {
    private String currentViewName;
    private final List<ViewManagerModelListener> listeners = new ArrayList<>();

    /**
     * Return current view
     */
    public String getCurrentViewName() {
        return currentViewName;
    }

    /**
     * Sets current view by changing view name and notifying the views
     * @param currentViewName - name of view
     */
    public void setCurrentViewName(String currentViewName) {
        this.currentViewName = currentViewName;

        for (ViewManagerModelListener listener : listeners) {
            listener.viewChange();
        }
    }

    /**
     * Adds a new listener that should be notified when view changes
     * @param listener - listener to add
     */

    public void addListener(ViewManagerModelListener listener) {
        listeners.add(listener);
    }
}
