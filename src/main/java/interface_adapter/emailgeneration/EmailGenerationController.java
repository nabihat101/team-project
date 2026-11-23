package interface_adapter.emailgeneration;

import use_case.EmailGeneration.EmailGenerationInputBoundary;
import use_case.EmailGeneration.EmailGenerationInputData;


public class EmailGenerationController {
    private final EmailGenerationInputBoundary interactor;

    /**
     * initializes the fields for class
     *
     * @param emailGenerationInteractor - interactor for the use case
     */
    public EmailGenerationController(EmailGenerationInputBoundary emailGenerationInteractor) {
        this.interactor = emailGenerationInteractor;
    }

    /**
     * Executes the email generation use case with param
     *
     * @param userBrief the user email purpose
     */
    public void execute(String userBrief) {
        final EmailGenerationInputData inputData = new EmailGenerationInputData(userBrief);
        interactor.execute(inputData);
    }
}
