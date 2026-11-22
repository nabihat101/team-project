package view;

import entities.ResearchPaper;
import interface_adapter.professor_pages.ProfessorPageController;
import interface_adapter.professor_pages.ProfessorPageState;
import interface_adapter.professor_pages.ProfessorPageViewModel;
import interface_adapter.professor_pages.ProfessorPageViewModelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import interface_adapter.ViewManagerModel;

// May need to change the imports depending on name
//import interface_adapter.landing.LandingViewModel;
//import interface_adapter.user_profile.UserProfileViewModel;

/**
 * Displays a professor's information and publications.
 */
public class ProfessorPageView extends JPanel implements ActionListener, ProfessorPageViewModelListener {

    private final ViewManagerModel viewManagerModel;
    private static final Color NAVY = new Color(0x0F1B2D);
    private static final Color BLUE = new Color(0x1D4ED8);
    private static final Color GRAY = new Color(0x5B667E);
    private static final Color LIGHT_GRAY = new Color(0xC9CFDD);
    private static final String FONT = "SansSerif";

    private final String viewName = "professor page";
    private final ProfessorPageViewModel professorPageViewModel;

    private final JLabel nameLabel = new JLabel(" ");
    private final JLabel institutionLabel = new JLabel(" ");
    private final JLabel researchAreasLabel = new JLabel(" ");
    private final JLabel errorLabel = new JLabel(" ");
    private final JLabel publicationsMessage = new JLabel(" ");

    private final JPanel publicationsPanel = new JPanel();
    private ProfessorPageController professorPageController;

    private final JButton homeButton = new JButton("Home");
    private final JButton profileButton = new JButton("My Profile");


    /**
     * Creates the professor page and listens for view model changes.
     *
     * @param professorPageViewModel the view model holding the page's state
     */
    public ProfessorPageView(ProfessorPageViewModel professorPageViewModel, ViewManagerModel viewManagerModel) {
        this.professorPageViewModel = professorPageViewModel;
        this.viewManagerModel = viewManagerModel;
        this.professorPageViewModel.addListener(this);
        homeButton.addActionListener(this);
        profileButton.addActionListener(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 560));
        setBackground(Color.WHITE);

        add(makeHeaderPanel(), BorderLayout.NORTH);
        add(makeContentPanel(), BorderLayout.CENTER);
    }

    /**
     * Makes the navy header containing the professor's name and institution.
     *
     * @return the header panel
     */
    private JPanel makeHeaderPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAVY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        nameLabel.setFont(new Font(FONT, Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);

        final JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navigationPanel.setBackground(NAVY);

        navigationPanel.add(homeButton);
        navigationPanel.add(profileButton);

        institutionLabel.setFont(new Font(FONT, Font.PLAIN, 15));
        institutionLabel.setForeground(new Color(0xC3CCE0));

        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        institutionLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(institutionLabel);

        return panel;
    }

    /**
     * Makes the main content area containing research areas and publications.
     *
     * @return the content panel
     */
    private JPanel makeContentPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        final JLabel researchHeading = makeHeading("Research Areas");
        final JLabel publicationsHeading = makeHeading("Publications");

        researchAreasLabel.setFont(new Font(FONT, Font.PLAIN, 14));
        researchAreasLabel.setForeground(GRAY);

        errorLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        errorLabel.setForeground(new Color(0xB91C1C));

        publicationsMessage.setFont(new Font(FONT, Font.PLAIN, 13));
        publicationsMessage.setForeground(GRAY);

        publicationsPanel.setLayout(new BoxLayout(publicationsPanel, BoxLayout.Y_AXIS));
        publicationsPanel.setBackground(Color.WHITE);

        final JScrollPane scrollPane = new JScrollPane(publicationsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        scrollPane.setPreferredSize(new Dimension(700, 220));
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(researchHeading);
        panel.add(Box.createVerticalStrut(8));
        panel.add(researchAreasLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(publicationsHeading);
        panel.add(Box.createVerticalStrut(12));
        panel.add(errorLabel);
        panel.add(publicationsMessage);
        panel.add(scrollPane);

        return panel;
    }

    /**
     * Makes a heading for a section of the page.
     *
     * @param text the heading text
     * @return the heading label
     */
    private JLabel makeHeading(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT, Font.BOLD, 18));
        label.setForeground(NAVY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Makes a panel displaying an individual research paper.
     *
     * @param paper the research paper to display
     * @return the publication panel
     */
    private JPanel makePublicationPanel(ResearchPaper paper) {
        final JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, LIGHT_GRAY),
                new EmptyBorder(12, 10, 12, 10)));

        final JLabel title = new JLabel(paper.getTitle());
        title.setFont(new Font(FONT, Font.BOLD, 14));
        title.setForeground(NAVY);

        final JLabel year = new JLabel(String.valueOf(paper.getYear()));
        year.setFont(new Font(FONT, Font.PLAIN, 12));
        year.setForeground(GRAY);

        final JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBackground(Color.WHITE);
        details.add(title);
        details.add(Box.createVerticalStrut(5));
        details.add(year);

        final JButton viewPaperButton = new JButton("View Paper");
        viewPaperButton.setFont(new Font(FONT, Font.BOLD, 12));
        viewPaperButton.setForeground(BLUE);
        viewPaperButton.setContentAreaFilled(false);
        viewPaperButton.setBorderPainted(false);
        viewPaperButton.setFocusPainted(false);
        viewPaperButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        viewPaperButton.addActionListener(evt -> {
            try {
                Desktop.getDesktop().browse(URI.create(paper.getUrl()));
            }
            catch (Exception e) {
                errorLabel.setText("Unable to open this paper.");
            }
        });

        panel.add(details, BorderLayout.CENTER);
        panel.add(viewPaperButton, BorderLayout.EAST);

        return panel;
    }

    /**
     * Updates the screen when the view model's state changes.
     */
    @Override
    public void stateChanged() {
        final ProfessorPageState state = professorPageViewModel.getState();

        nameLabel.setText(state.getProfessor().getName());
        institutionLabel.setText(state.getProfessor().getInstitution());
        researchAreasLabel.setText(String.join(", ", state.getProfessor().getResearchAreas()));

        errorLabel.setText(" ");
        publicationsPanel.removeAll();

        if (state.getError() != null && !state.getError().isBlank()) {
            errorLabel.setText(state.getError());
        }

        if (state.getProfessor().getPublications() == null || state.getProfessor().getPublications() .isEmpty()) {
            publicationsMessage.setText("No publications found.");
        }
        else {
            publicationsMessage.setText(" ");

            for (ResearchPaper paper : state.getProfessor().getPublications() ) {
                publicationsPanel.add(makePublicationPanel(paper));
            }
        }

        publicationsPanel.revalidate();
        publicationsPanel.repaint();
        revalidate();
        repaint();
    }

    /**
     * Returns the name used to switch to this view.
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the controller used by this view.
     * @param professorPageController the professor page controller
     */
    public void setProfessorPageController(ProfessorPageController professorPageController) {
        this.professorPageController = professorPageController;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == homeButton) {
            //viewManagerModel.setCurrentViewName(LandingViewModel.VIEW_NAME);
        }
        else if (evt.getSource() == profileButton) {
            //viewManagerModel.setCurrentViewName(UserProfileViewModel.VIEW_NAME);
        }
    }
}
