package interface_adapter.PaperOfTheDay;

import use_case.PaperOfTheDayUseCase.PaperOfTheDayInputBoundary;
import use_case.PaperOfTheDayUseCase.PaperOfTheDayInputData;

/** Controller for the Paper of the Day use case. */
public class PaperOfTheDayController {
    private final PaperOfTheDayInputBoundary interactor;

    public PaperOfTheDayController(PaperOfTheDayInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the Paper of the Day for the given user.
     * @param username the logged-in user's username
     */
    public void execute(String username) {
        PaperOfTheDayInputData inputData = new PaperOfTheDayInputData(username);
        interactor.execute(inputData);
    }

}