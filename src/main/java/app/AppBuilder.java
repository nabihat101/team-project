package app;

import data_access.PaperOfTheDayDataObject;
import data_access.PaperSearchDataObject;
import data_access.ProfessDataObject;
import data_access.RankedProfessorDAO;
import interface_adapter.PaperOfTheDay.PaperOfTheDayController;
import interface_adapter.PaperOfTheDay.PaperOfTheDayPresenter;
import interface_adapter.PaperOfTheDay.PaperOfTheDayViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.professor_pages.ProfessorPageController;
import interface_adapter.professor_pages.ProfessorPagePresenter;
import interface_adapter.professor_pages.ProfessorPageViewModel;
import interface_adapter.rankedProf.RankedProfessorController;
import interface_adapter.rankedProf.RankedProfessorPresenter;
import interface_adapter.rankedProf.RankedProfessorState;
import interface_adapter.rankedProf.RankedProfessorViewModel;
import use_case.LoginUseCase.*;
import use_case.PaperOfTheDayUseCase.*;
import use_case.RankedProfessorCase.RankedProfessorDataAccessInterface;
import use_case.RankedProfessorCase.RankedProfessorInputBoundary;
import use_case.RankedProfessorCase.RankedProfessorInteractor;
import use_case.RankedProfessorCase.RankedProfessorOutputBoundary;
import use_case.professor_pages.*;
import view.*;

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

    private ProfessorPageView professorPageView;
    private ProfessorPageViewModel professorPageViewModel;
    private ProfessorDataAccessInterface professorDataAccessObject;

    private PaperOfTheDayView paperOfTheDayView;
    private PaperOfTheDayViewModel paperOfTheDayViewModel;

    private RankedProfessorView rankedProfView;
    private RankedProfessorViewModel rankedProfViewModel;
    private ProfessorPageController professorPageController;

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
     * Creates the professor page view.
     * @return this AppBuilder
     */
    public AppBuilder addProfessorPageView() {
        professorPageViewModel = new ProfessorPageViewModel();
        professorPageView = new ProfessorPageView(professorPageViewModel, viewManagerModel);
        cardPanel.add(professorPageView, ProfessorPageViewModel.VIEW_NAME);
        return this;
    }


    /**
     * Creates the professor page use case and initializes its objects.
     * @return this AppBuilder
     */
    public AppBuilder addProfessorPageUseCase() {
        ProfessorPageOutputBoundary professorPagePresenter = new ProfessorPagePresenter(professorPageViewModel);
        professorDataAccessObject = new ProfessDataObject();
        ProfessorPageInputBoundary professorPageInteractor =
                new ProfessorPageInteractor(professorDataAccessObject, professorPagePresenter);
        professorPageController = new ProfessorPageController(professorPageInteractor);
        professorPageView.setProfessorPageController(professorPageController);
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

        // when the app switches to Paper of the Day, load it for whoever is logged in
        viewManagerModel.addListener(() -> {
            if (PaperOfTheDayViewModel.VIEW_NAME.equals(viewManagerModel.getCurrentViewName())) {
                paperOfTheDayView.loadFor(loginDataObject.getUsername());
            }
        });
        return this;
    }

    /**
     * Creates the Ranked Professor view.
     * @return this AppBuilder
     */
    public AppBuilder addRankedProfPage() {
        rankedProfViewModel = new RankedProfessorViewModel();
        rankedProfView = new RankedProfessorView(rankedProfViewModel, viewManagerModel);
        cardPanel.add(rankedProfView, RankedProfessorViewModel.VIEW_NAME);
        return this;
    }


    /**
     * Creates the Ranked Professor use case and connects it to the view.
     * @return this AppBuilder
     */
    public AppBuilder addRankedProfUseCase() {
        RankedProfessorDAO rankedProfDao = new RankedProfessorDAO();
        RankedProfessorOutputBoundary presenter = new RankedProfessorPresenter(rankedProfViewModel);
        RankedProfessorDataAccessInterface rankedProfDataAccessInterface =
                new RankedProfessorDAO();
        RankedProfessorInputBoundary interactor =
                new RankedProfessorInteractor(loginDataObject, rankedProfDao, presenter);

        rankedProfView.setRankedProfessorController(new RankedProfessorController(interactor));
        rankedProfView.setProfessorPageController(professorPageController);

        loadFilterOptions(rankedProfDao);

        // when the app switches to Paper of the Day, load it for whoever is logged in
        viewManagerModel.addListener(() -> {
            if (RankedProfessorViewModel.VIEW_NAME.equals(viewManagerModel.getCurrentViewName())) {
                rankedProfView.loadFor(loginDataObject.getUsername());
            }
        });
        return this;
    }

    /** Loads the checkbox options once and puts them in the view model's state. */
    private void loadFilterOptions(RankedProfessorDAO dao) {
        final RankedProfessorState state = rankedProfViewModel.getState();
        try {
            state.setTopicOptions(dao.getTopicOptions());
            state.setLocationOptions(dao.getLocationOptions());
        }
        catch (RuntimeException exception) {
            state.setError("Couldn't load the filter options. Please restart and try again.");
        }
        rankedProfViewModel.setState(state);
        rankedProfViewModel.notifyStateChanged();
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
