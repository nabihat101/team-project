package use_case.PaperOfTheDayUseCase;

/**
 * The output boundary for the Paper Of The Day use case
 */
public interface PaperOfTheDayOutputBoundary {

    /**
     * Prepares the success view for the Paper of the Day Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(PaperOfTheDayOutputData outputData);

    /**
     * Prepares the failure view for the Paper of the Day Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
