package view;
import interface_adapter.ViewManagerModel;
import interface_adapter.userprofile.UserProfileViewModel;

import javax.swing.*;

public class UserProfileViewRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            UserProfileViewModel viewModel = new UserProfileViewModel();
            ViewManagerModel viewManagerModel = new ViewManagerModel();

            UserProfileView loginView = new UserProfileView(viewModel, viewManagerModel);

            JFrame frame = new JFrame("Login View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(loginView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
