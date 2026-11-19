package use_case.LoginUseCase;

/**
 *  The input boundary for the login use case
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     * @param loginInputData the input data
     */
    void execute(LoginInputData loginInputData);

}
