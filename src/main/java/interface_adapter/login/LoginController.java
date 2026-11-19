package interface_adapter.login;
import use_case.LoginUseCase.LoginInputBoundary;
import use_case.LoginUseCase.LoginInputData;

/**
 * class for controller of login use case
 */
public class LoginController {
    private final LoginInputBoundary interactor;

    /**
     * initializes the fields for class
     * @param loginInteractor - interactor for the use case
     */
    public LoginController(LoginInputBoundary loginInteractor) {
        this.interactor = loginInteractor;
    }

    /**
     * Executes the login use case with param.
     * @param username the username of user
     * @param password the password of user's profile
     */
    public void execute(String username, String password) {
        final LoginInputData loginInputData = new LoginInputData(username, password);

        interactor.execute(loginInputData);
    }

}
