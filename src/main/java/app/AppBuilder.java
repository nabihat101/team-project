package app;

import interface_adapter.PaperOfTheDay.PaperOfTheDayController;
import interface_adapter.PaperOfTheDay.PaperOfTheDayPresenter;
import interface_adapter.PaperOfTheDay.PaperOfTheDayViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import use_case.LoginUseCase.*;
import use_case.PaperOfTheDayUseCase.*;
import view.LoginView;
import view.PaperOfTheDayView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

/**
 * Builds the app by connecting each view to its use case.
 */
public class AppBuilder {

    private JPanel cardPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private LoginDataObject loginDataObject = new LoginDataObject();

    private PaperOfTheDayView paperOfTheDayView;
    private PaperOfTheDayViewModel paperOfTheDayViewModel;

    /**
     * builds components of application together
     */
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Creates login view
     * @return this AppBuilder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Creates the login use case and connects it to the view
     * @return this AppBuilder
     */
    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary loginOutputBoundary = new LoginPresenter(loginViewModel, viewManagerModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginDataObject, loginOutputBoundary);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Creates the Paper of the Day view.
     * @return this AppBuilder
     */
    public AppBuilder addPaperOfTheDayView() {
        paperOfTheDayViewModel = new PaperOfTheDayViewModel();
        paperOfTheDayView = new PaperOfTheDayView(paperOfTheDayViewModel);
        cardPanel.add(paperOfTheDayView, PaperOfTheDayViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Creates the Paper of the Day use case and connects it to the view.
     * @return this AppBuilder
     */
    public AppBuilder addPaperOfTheDayUseCase() {
        PaperOfTheDayOutputBoundary presenter = new PaperOfTheDayPresenter(paperOfTheDayViewModel);
        PaperOfTheDayDataAccessInterface paperOfTheDayData =
                new PaperOfTheDayDataObject("paper_of_the_day.json", loginDataObject);
        PaperSearchInterface paperSearch = new PaperSearchDataObject();
        PaperOfTheDayInputBoundary interactor =
                new PaperOfTheDayInteractor(paperSearch, paperOfTheDayData, presenter);
        paperOfTheDayView.setPaperOfTheDayController(new PaperOfTheDayController(interactor));
        return this;
    }

    /**
     * FOR TESTING: skips login and opens Paper of the Day for a user.
     * @param username an existing user
     * @return this AppBuilder
     */
    public AppBuilder showPaperOfTheDayFor(String username) {
        viewManagerModel.setCurrentViewName(PaperOfTheDayViewModel.VIEW_NAME);
        paperOfTheDayView.loadFor(username);
        return this;
    }

    /**
     * Creates main application window
     * @return application view
     */
    public JFrame build() {
        JFrame application = new JFrame("CS Research Finder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        // start on the login screen
        viewManagerModel.setCurrentViewName(LoginViewModel.VIEW_NAME);

        application.pack();
        application.setLocationRelativeTo(null);
        return application;
    }
}