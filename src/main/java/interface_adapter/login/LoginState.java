package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String username = "";
    private String loginError;
    private String password = "";

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the login error
     */
    public String getLoginError() {
        return loginError;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the login error.
     * @param loginError the login error to set
     */
    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    /**
     * Sets the password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}