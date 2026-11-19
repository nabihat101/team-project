package use_case.LoginUseCase;

/**
 * The input data for the login use case
 */
public class LoginInputData {

    private final String username;
    private final String password;

    /**
     *
     * @param username  The user's username
     * @param password  The user's password
     */
    public LoginInputData(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @return username
     */

    public String getUsername(){
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword(){
        return password;
    }
}
