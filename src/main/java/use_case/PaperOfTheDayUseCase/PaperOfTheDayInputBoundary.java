package use_case.PaperOfTheDayUseCase;

/**
 * Input Boundary for actions which are related to the paper of the day.
 */

public interface PaperOfTheDayInputBoundary {
    /**
     * Executes the paper of the day use case.
     * @param paperOfTheDayInputData the input data
     */
    void execute(PaperOfTheDayInputData paperOfTheDayInputData);
}
