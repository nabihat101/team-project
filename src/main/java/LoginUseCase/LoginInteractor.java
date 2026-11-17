package LoginUseCase;

import java.util.List;
import entities.User;

public class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByUsername(username)) {
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