package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginViewModelListener;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The login screen: a navy panel on the left and the login form on the right.
 */
public class LoginView extends JPanel implements ActionListener, LoginViewModelListener {

    private static final Color NAVY = new Color(0x0F1B2D);
    private static final Color BLUE = new Color(0x1D4ED8);
    private static final Color GRAY = new Color(0x5B667E);
    private static final Color LIGHT_GRAY = new Color(0xC9CFDD);
    private static final String FONT = "SansSerif";

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JCheckBox showPasswordBox = new JCheckBox("Show password");
    private final char defaultEchoChar = passwordField.getEchoChar();
    private final JLabel errorLabel = new JLabel(" ");
    private final JButton loginButton = new JButton("Log in");
    private final JButton createAccountButton = new JButton("Sign up");
    private LoginController loginController;

    /**
     * Creates the login view and listens for view model changes
     *
     * @param loginViewModel the view model that holds the screen's state
     */
    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addListener(this);

        setLayout(new GridLayout(1, 2));
        setPreferredSize(new Dimension(900, 560));
        add(makeNamePanel());
        add(makeFormPanel());

        loginButton.addActionListener(this);
        passwordField.addActionListener(this);
        createAccountButton.addActionListener(this);

        // show or hide the password characters
        showPasswordBox.addActionListener(evt -> {
            if (showPasswordBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            }
            else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });
    }

    /**
     * Makes the panel on the left with the app name
     *
     * @return the left panel
     */
    private JPanel makeNamePanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 48, 0, 48));

        final JLabel title = new JLabel("CS Research Finder");
        title.setFont(new Font(FONT, Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        final JLabel tagline = new JLabel("Find new papers and the professors behind them.");
        tagline.setFont(new Font(FONT, Font.PLAIN, 15));
        tagline.setForeground(new Color(0xC3CCE0));

        title.setAlignmentX(LEFT_ALIGNMENT);
        tagline.setAlignmentX(LEFT_ALIGNMENT);

        // glue on the top and bottom pushes the text to the vertical middle
        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(tagline);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    /**
     * Makes the panel on the right with the login form
     *
     * @return the form panel
     */
    private JPanel makeFormPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 75, 0, 75));

        final JLabel heading = new JLabel("Welcome back!");
        heading.setFont(new Font(FONT, Font.BOLD, 26));
        heading.setForeground(NAVY);

        final JLabel subheading = new JLabel("Log in to track professors and papers.");
        subheading.setForeground(GRAY);

        styleField(usernameField);
        styleField(passwordField);

        showPasswordBox.setFont(new Font(FONT, Font.PLAIN, 12));
        showPasswordBox.setForeground(GRAY);
        showPasswordBox.setOpaque(false);
        showPasswordBox.setFocusPainted(false);
        showPasswordBox.setMargin(new Insets(0, 0, 0, 0));
        showPasswordBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        errorLabel.setForeground(new Color(0xB91C1C));

        loginButton.setFont(new Font(FONT, Font.BOLD, 15));
        loginButton.setBackground(BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 42));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        createAccountButton.setForeground(BLUE);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setMargin(new Insets(0, 6, 0, 0));
        createAccountButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // "New here? Sign up"
        final JPanel signupRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        signupRow.setBackground(Color.WHITE);
        signupRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        final JLabel newHere = new JLabel("New here?");
        newHere.setForeground(GRAY);
        signupRow.add(newHere);
        signupRow.add(createAccountButton);

        heading.setAlignmentX(LEFT_ALIGNMENT);
        subheading.setAlignmentX(LEFT_ALIGNMENT);
        usernameField.setAlignmentX(LEFT_ALIGNMENT);
        passwordField.setAlignmentX(LEFT_ALIGNMENT);
        showPasswordBox.setAlignmentX(LEFT_ALIGNMENT);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);
        loginButton.setAlignmentX(LEFT_ALIGNMENT);
        signupRow.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(heading);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subheading);
        panel.add(Box.createVerticalStrut(24));
        panel.add(makeLabel("Username"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(makeLabel("Password"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(6));
        panel.add(showPasswordBox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(16));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(signupRow);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    /**
     * Makes a small bold label for above a text field.
     *
     * @param text the label text
     * @return the label
     */
    private JLabel makeLabel(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT, Font.BOLD, 12));
        label.setForeground(GRAY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Gives a text field a thin border with some padding inside.
     *
     * @param field the field to style
     */
    private void styleField(JTextField field) {
        field.setFont(new Font(FONT, Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        // without a maximum height, BoxLayout can stretch the field too tall
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    /**
     * Handles clicks on the buttons and enter in the password field
     *
     * @param evt the event that happened
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == createAccountButton) {
            // switch to the signup view
        }
        else if (loginController != null) {
            loginController.execute(usernameField.getText(),
                    new String(passwordField.getPassword()));
        }
    }

    /**
     * Updates the screen when the view model's state changes
     */
    @Override
    public void stateChanged() {
        final LoginState state = loginViewModel.getState();
        final String error = state.getLoginError();
        if (error == null || error.isBlank()) {
            errorLabel.setText(" ");
        }
        else {
            errorLabel.setText(error);
            passwordField.setText("");
        }
    }

    /**
     * Returns the name used to switch to this view
     *
     * @return the view name
     */
    public String getViewName() {

        return viewName;
    }

    /**
     * Sets the controller that this view calls when the user acts
     *
     * @param loginController the login controller
     */
    public void setLoginController(LoginController loginController) {

        this.loginController = loginController;
    }
}