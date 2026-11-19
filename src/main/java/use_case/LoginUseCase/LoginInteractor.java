package use_case.LoginUseCase;

import entities.User;


/** The main LoginInteractor class that handles the login use case logic. 
 * - Interacts with the data access layer to verify user credentials.
 * - Communicates with the presenter through the LoginOutputBoundary Interfaceto prepare the appropriate view based on the login outcome.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    /** Constructor for class 
     * @param userDataAccessInterface the data access interface for user data
     * @param loginOutputBoundary the output boundary for the login use case
    */
    public LoginInteractor(LoginDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    /**
     * Executes the login use case. Checks if the username exists in the data access layer. If it does, it verifies the password. 
     * Depending on the outcome, it prepares either a success or failure view through the presenter.
     * @param loginInputData the input data
     * @returns void
     */
    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if(!userDataAccessObject.existsByUsername(username) && username.isEmpty()){
            loginPresenter.prepareFailureView("Please enter a valid username.");
        }

        else if (!userDataAccessObject.existsByUsername(username)) {
            loginPresenter.prepareFailureView(username + ": Account does not exist.");
        } else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailureView("Incorrect password for \"" + username + "\".");
            } else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setUsername(username);

                final Login_OutputData loginOutputData = new Login_OutputData(user.getUsername());
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}