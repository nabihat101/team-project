package LoginUseCase;


/**
 * The output data class for the login use case
 */
public class LoginOutputData {
    private String username;


    /**
     * Initializes the username for the object
     */
    public LoginOutputData (String username) {
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
