package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.loggedin.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import use_case.LoginUseCase.*;
import view.LoginView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class AppBuilder {

    private JPanel cardPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private LoginDataObject loginDataObject = new LoginDataObject();

    /**
     * builds components of application together
     */
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Created login view
     * @return this AppBuilder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);

        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);

        return this;
    }

    /**
     * Creates the use case and init. all objects
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
     * Creates main application window
     * @return application view
     */
    public JFrame build() {
        JFrame application = new JFrame("CS Research Finder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        //set initial to be login
        viewManagerModel.setCurrentViewName(LoginViewModel.VIEW_NAME);

        application.pack();
        application.setLocationRelativeTo(null);

        return application;
    }
}
