package interface_adapter.PaperOfTheDay;

/**
 * Something that wants to know when the Paper of the Day state changes (the view).
 */
public interface PaperOfTheDayViewModelListener {
    void stateChanged();
}
