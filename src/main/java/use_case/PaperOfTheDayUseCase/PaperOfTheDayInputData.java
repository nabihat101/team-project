package use_case.PaperOfTheDayUseCase;

/**
 * The Input Data for the Paper of the Day Use Case.
 */
public class PaperOfTheDayInputData {
    private final String username;

    public PaperOfTheDayInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }

}
