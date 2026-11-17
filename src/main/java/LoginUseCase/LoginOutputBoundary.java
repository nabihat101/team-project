package LoginUseCase;

/**
 * Output boundary for login use case
 */
public interface LoginOutputBoundary {
    /**
     * Creates the view for if successfully logged in
     * @param outputData is the object
     */

    void prepareSuccessView(Login_OutputData outputData);

    /**
     * Creates the view if error occurs during login
     * @param message is the error message
     */

    void prepareFailureView(String message);


}
