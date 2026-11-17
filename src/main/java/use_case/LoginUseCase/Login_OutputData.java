package use_case.LoginUseCase;


/**
 * The output data class for the login use case
 */
public class Login_OutputData {
    private String username;

    /**
     * Initializes the username for the object
     */
    public Login_OutputData (String username) {
        this.username = username;
    }

    /**
     * Initializes the username for the object
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }
}
