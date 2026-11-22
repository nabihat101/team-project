package use_case.EmailGeneration;

public interface EmailGenerationInputBoundary {
    /**
     * Executes the email generation use case.
     * @param emailGenerationInputData the input data
     */
    void execute(EmailGenerationInputData emailGenerationInputData);
}
