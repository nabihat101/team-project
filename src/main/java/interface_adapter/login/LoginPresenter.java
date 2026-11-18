package interface_adapter.login;

// import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.loggedin.LoggedInState;
import interface_adapter.loggedin.LoggedInViewModel;
import use_case.LoginUseCase.LoginOutputBoundary;
import use_case.LoginUseCase.Login_OutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructor for the LoginPresenter.
     * @param loggedInViewModel - the view model for the logged in view
     * @param loginViewModel - the view model for the login view
     */
    public LoginPresenter( // ViewManagerModel viewManagerModel,
                          // LoggedInViewModel loggedInViewModel,
                           LoginViewModel loginViewModel, ViewManagerModel viewManagerModel) {
        // this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Prepares the success view for the login use case.
     * @param response - the output data from the login use case
     */
    @Override
    public void prepareSuccessView(Login_OutputData response) {
        // On success, update the loggedInViewModel's state
        //final LoggedInState loggedInState = loggedInViewModel.getState();
        //loggedInState.setUsername(response.getUsername());
        //this.loggedInViewModel.notifyStateChanged();

        // and clear everything from the LoginViewModel's state
        this.loginViewModel.setState(new LoginState());
        this.loginViewModel.notifyStateChanged();

        // switch to the logged in view
        // this.viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        // this.viewManagerModel.notifyViewChanged();
    }

    /**
     * Prepares the failure view for the login use case.
     * @param error - the error message from the login use case
     */
    @Override
    public void prepareFailureView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.notifyStateChanged();
    }
}