package use_case.login;

import entities.UserFactory;
import entities.User;
import use_case.LoginUseCase.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LoginInteractorTest {

    @Test
    void successTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");
        LoginDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        UserFactory factory = new UserFactory();
        User user = factory.create("Paul", "password", List.of("ML", "CompSci"),
                List.of("UofT", "UBC"));
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(Login_OutputData user) {
                assertEquals("Paul", user.getUsername());
                assertEquals("Paul", userRepository.getUsername());
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() {
        LoginInputData inputData = new LoginInputData("Paul", "wrong");
        LoginDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For this failure test, we need to add Paul to the data access repository before we log in, and
        // the passwords should not match.
        UserFactory factory = new UserFactory();
        User user = factory.create("Paul", "password", List.of("ML", "CompSci"),
                List.of("UofT", "UBC"));
        userRepository.save(user);

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(Login_OutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailureView(String error) {
                assertEquals("Incorrect password for \"Paul\".", error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");
        LoginDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // Add Paul to the repo so that when we check later they already exist

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(Login_OutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailureView(String error) {
                assertEquals("Paul: Account does not exist.", error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}