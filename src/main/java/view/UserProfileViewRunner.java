package view;
import data_access.UserProfileDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.userprofile.UserProfileController;
import interface_adapter.userprofile.UserProfilePresenter;
import interface_adapter.userprofile.UserProfileViewModel;
import use_case.UserProfile.UserProfileInteractor;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.nio.file.Path;

public class UserProfileViewRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            UserProfileViewModel viewModel = new UserProfileViewModel();
            ViewManagerModel viewManagerModel = new ViewManagerModel();

            UserProfileView profileView = new UserProfileView(viewModel, viewManagerModel);
            UserProfileDataAccessObject dataAccess =
                    new UserProfileDataAccessObject(Path.of("users.json"));
            UserProfilePresenter presenter = new UserProfilePresenter(viewModel);
            UserProfileInteractor interactor = new UserProfileInteractor(dataAccess, presenter);
            UserProfileController controller = new UserProfileController(interactor);
            profileView.setUserProfileController(controller);
            controller.execute("iman");

            JFrame frame = new JFrame("User Profile View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(profileView);
            java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screen.width * 0.96);
            int height = (int) (screen.height * 0.94);
            frame.setMinimumSize(new Dimension(Math.min(760, width), Math.min(560, height)));
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
