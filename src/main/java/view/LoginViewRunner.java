package view;

import interface_adapter.login.LoginViewModel;

import javax.swing.*;

/**
 * only for testing that the login view looks correct - delete later
 */
public class LoginViewRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            LoginViewModel viewModel = new LoginViewModel();

            LoginView loginView = new LoginView(viewModel);

            JFrame frame = new JFrame("Login View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(loginView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
