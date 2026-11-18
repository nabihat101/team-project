package interface_adapter.login;

/**
 * Login state for login view model
 */
public class LoginState {

    private String username = "";
    private String loginError;
    private String password = "";

    /**
     * @return username
     */
    public String getUserName() {
        return username;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginError(String usernameError) {
        this.loginError = usernameError;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
