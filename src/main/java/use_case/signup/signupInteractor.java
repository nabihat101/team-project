package use_case.signup;

import entities.User;
import entities.UserFactory;

/**
 * The Signup Interactor.
 */
public class signupInteractor implements signupInputBoundary {
    private final signupDAInterface userDataAccessObject;
    private final signupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public signupInteractor(signupDAInterface signupDataAccessInterface,
                            signupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(signupInputData signupInputData) {
        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            userPresenter.prepareFailView("User already exists.");
        }
        else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else if ("".equals(signupInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if ("".equals(signupInputData.getUsername())) {
            userPresenter.prepareFailView("Username cannot be empty");
        }
        else {
            final User user = userFactory.create(signupInputData.getUsername(),
                    signupInputData.getPassword(), signupInputData.getTopics(),
                    signupInputData.getLocations());
            userDataAccessObject.save(user);

            final signupOutputData signupOutputData = new signupOutputData(user.getUsername());
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}