package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.professor_pages.ProfessorPageController;
import interface_adapter.professor_pages.ProfessorPageViewModel;
import interface_adapter.rankedProf.RankedProfessorController;
import interface_adapter.rankedProf.RankedProfessorState;
import interface_adapter.rankedProf.RankedProfessorViewModel;
import interface_adapter.rankedProf.RankedProfessorViewModelListener;
import interface_adapter.PaperOfTheDay.PaperOfTheDayViewModel;
import use_case.RankedProfessorCase.RankedProfessorEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.Rectangle;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/**
 * IMPORTANT NOTE: THIS FILE HAS BEEN VIBECODED. THE PROJECT ALLOWS THE GUI TO BE VIBE CODED.
 */

/**
 * The home page: checkbox filters on the left and the ranked list of professors on the right.
 */
public class RankedProfessorView extends JPanel
        implements ActionListener, RankedProfessorViewModelListener {

    private static final Color NAVY = new Color(0x0F1B2D);
    private static final Color BLUE = new Color(0x1D4ED8);
    private static final Color GRAY = new Color(0x5B667E);
    private static final Color LIGHT_GRAY = new Color(0xC9CFDD);
    private static final String FONT = "SansSerif";

    private final RankedProfessorViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private final JLabel criteriaLabel = new JLabel(" ");
    private final JLabel errorLabel = new JLabel(" ");
    private final JLabel messageLabel = new JLabel(" ");
    private final JButton searchButton = new JButton("Search");
    private final JPanel topicsPanel = new JPanel();
    private final JPanel locationsPanel = new JPanel();
    private final JPanel resultsPanel = new WidthTrackingPanel();

    private final List<JCheckBox> topicBoxes = new ArrayList<>();
    private final List<JCheckBox> locationBoxes = new ArrayList<>();
    private List<String> shownTopicOptions = new ArrayList<>();
    private List<String> shownLocationOptions = new ArrayList<>();

    private RankedProfessorController rankedProfessorController;
    private ProfessorPageController professorPageController;
    private String username = "";

    private final JButton paperButton = new JButton("Paper of the Day");

    /**
     * Creates the home page and listens for view model changes.
     *
     * @param viewModel the view model that holds this page's state
     * @param viewManagerModel used to switch to the professor page
     */
    public RankedProfessorView(RankedProfessorViewModel viewModel,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.viewModel.addListener(this);

        searchButton.addActionListener(this);
        paperButton.addActionListener(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 560));
        setBackground(Color.WHITE);
        add(makeHeaderPanel(), BorderLayout.NORTH);
        add(makeFilterPanel(), BorderLayout.WEST);
        add(makeResultsPanel(), BorderLayout.CENTER);
    }

    /**
     * Makes the navy header with the page title and the criteria currently used.
     *
     * @return the header panel
     */
    private JPanel makeHeaderPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        final JLabel title = new JLabel("Professors for you");
        title.setFont(new Font(FONT, Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        criteriaLabel.setFont(new Font(FONT, Font.PLAIN, 14));
        criteriaLabel.setForeground(new Color(0xC3CCE0));

        title.setAlignmentX(LEFT_ALIGNMENT);
        criteriaLabel.setAlignmentX(LEFT_ALIGNMENT);

        paperButton.setFont(new Font(FONT, Font.BOLD, 13));
        paperButton.setBackground(BLUE);
        paperButton.setForeground(Color.WHITE);
        paperButton.setOpaque(true);
        paperButton.setBorderPainted(false);
        paperButton.setFocusPainted(false);
        paperButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(paperButton, BorderLayout.EAST);
        titleRow.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(titleRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(criteriaLabel);
        return panel;
    }


    /** A panel that is always exactly as wide as the scroll pane showing it. */
    private static class WidthTrackingPanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (orientation == SwingConstants.VERTICAL) {
                return visibleRect.height;
            }
            return visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }


    /**
     * Makes the left panel with the topic and location checkboxes and the search button.
     *
     * @return the filter panel
     */
    private JPanel makeFilterPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 30, 20, 20)));

        styleCheckboxPanel(topicsPanel);
        styleCheckboxPanel(locationsPanel);

        searchButton.setFont(new Font(FONT, Font.BOLD, 15));
        searchButton.setBackground(BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(makeHeading("Research areas"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(makeScrollPane(topicsPanel));
        panel.add(Box.createVerticalStrut(18));
        panel.add(makeHeading("Locations"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(makeScrollPane(locationsPanel));
        panel.add(Box.createVerticalStrut(18));
        panel.add(searchButton);
        return panel;
    }

    /**
     * Makes the right panel with the message labels and the scrollable list of professors.
     *
     * @return the results panel
     */
    private JPanel makeResultsPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 40));

        errorLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        errorLabel.setForeground(new Color(0xB91C1C));
        messageLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        messageLabel.setForeground(GRAY);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        final JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);

        errorLabel.setAlignmentX(LEFT_ALIGNMENT);
        messageLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(errorLabel);
        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(10));
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
        label.setFont(new Font(FONT, Font.BOLD, 14));
        label.setForeground(NAVY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Sets up a panel to hold a vertical list of checkboxes.
     *
     * @param panel the panel to style
     */
    private void styleCheckboxPanel(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
    }

    /**
     * Wraps a checkbox panel in a bordered scroll pane.
     *
     * @param content the checkbox panel
     * @return the scroll pane
     */
    private JScrollPane makeScrollPane(JPanel content) {
        final JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        scrollPane.setPreferredSize(new Dimension(240, 170));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        return scrollPane;
    }

    /**
     * Fills a panel with one checkbox for each option, replacing any that were there.
     *
     * @param panel the panel to fill
     * @param boxes the list that keeps track of the checkboxes
     * @param options the names to show
     */
    private void fillCheckboxes(JPanel panel, List<JCheckBox> boxes, List<String> options) {
        panel.removeAll();
        boxes.clear();
        for (String option : options) {
            final JCheckBox box = new JCheckBox(option);
            box.setFont(new Font(FONT, Font.PLAIN, 13));
            box.setForeground(GRAY);
            box.setOpaque(false);
            box.setFocusPainted(false);
            boxes.add(box);
            panel.add(box);
        }
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Returns the names of the checkboxes that are ticked.
     *
     * @param boxes the checkboxes to look at
     * @return the ticked names, in order
     */
    private List<String> getSelected(List<JCheckBox> boxes) {
        final List<String> selected = new ArrayList<>();
        for (JCheckBox box : boxes) {
            if (box.isSelected()) {
                selected.add(box.getText());
            }
        }
        return selected;
    }

    /**
     * Makes one row of the results list for a professor.
     *
     * @param entry the professor to show
     * @return the row panel
     */
    private JPanel makeProfessorRow(RankedProfessorEntry entry) {
        final JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, LIGHT_GRAY),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        final JLabel name = new JLabel(entry.getName());
        name.setFont(new Font(FONT, Font.BOLD, 14));
        name.setForeground(NAVY);

        final JLabel institution = new JLabel(entry.getInstitution());
        institution.setFont(new Font(FONT, Font.PLAIN, 13));
        institution.setForeground(GRAY);

        final JLabel areas = new JLabel("Matched: " + String.join(", ", entry.getMatchedAreas()));
        areas.setFont(new Font(FONT, Font.PLAIN, 12));
        areas.setForeground(GRAY);

        final JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBackground(Color.WHITE);
        details.add(name);
        details.add(Box.createVerticalStrut(3));
        details.add(institution);
        details.add(Box.createVerticalStrut(3));
        details.add(areas);

        final JButton viewButton = new JButton("View Profile");
        viewButton.setFont(new Font(FONT, Font.BOLD, 12));
        viewButton.setForeground(BLUE);
        viewButton.setContentAreaFilled(false);
        viewButton.setBorderPainted(false);
        viewButton.setFocusPainted(false);
        viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(evt -> openProfessor(entry.getProfessorId()));

        row.add(details, BorderLayout.CENTER);
        row.add(viewButton, BorderLayout.EAST);
        return row;
    }

    /**
     * Opens a professor's page.
     *
     * @param professorId the OpenAlex id of the professor
     */
    private void openProfessor(String professorId) {
        if (professorPageController != null) {
            professorPageController.execute(professorId);
        }
        viewManagerModel.setCurrentViewName(ProfessorPageViewModel.VIEW_NAME);
    }

    /**
     * Runs the use case on a background thread so the window does not freeze.
     *
     * @param topics the ticked topics (empty means use the profile)
     * @param locations the ticked locations (empty means use the profile)
     */
    private void runSearch(List<String> topics, List<String> locations) {
        if (rankedProfessorController == null) {
            return;
        }
        searchButton.setEnabled(false);
        messageLabel.setText("Searching...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                rankedProfessorController.execute(username, topics, locations);
                return null;
            }

            @Override
            protected void done() {
                searchButton.setEnabled(true);
            }
        }.execute();
    }

    /**
     * Handles clicks on the search button.
     *
     * @param evt the event that happened
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == searchButton) {
            runSearch(getSelected(topicBoxes), getSelected(locationBoxes));
        }
        else if (evt.getSource() == paperButton) {
            viewManagerModel.setCurrentViewName(PaperOfTheDayViewModel.VIEW_NAME);
        }
    }

    /**
     * Updates the screen when the view model's state changes.
     */
    @Override
    public void stateChanged() {
        // the presenter may call this from the background thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::stateChanged);
            return;
        }

        final RankedProfessorState state = viewModel.getState();
        updateOptions(state);
        updateCriteria(state);
        updateResults(state);
    }

    /**
     * Rebuilds the checkboxes, but only if the available options changed,
     * so the user's ticks are kept after a search.
     *
     * @param state the current state
     */
    private void updateOptions(RankedProfessorState state) {
        if (!state.getTopicOptions().equals(shownTopicOptions)) {
            shownTopicOptions = new ArrayList<>(state.getTopicOptions());
            fillCheckboxes(topicsPanel, topicBoxes, shownTopicOptions);
        }
        if (!state.getLocationOptions().equals(shownLocationOptions)) {
            shownLocationOptions = new ArrayList<>(state.getLocationOptions());
            fillCheckboxes(locationsPanel, locationBoxes, shownLocationOptions);
        }
    }

    /**
     * Shows which topics and locations the current list is based on.
     *
     * @param state the current state
     */
    private void updateCriteria(RankedProfessorState state) {
        if (state.getTopicsUsed().isEmpty()) {
            criteriaLabel.setText(" ");
            return;
        }
        String locations = "all of Canada";
        if (!state.getLocationsUsed().isEmpty()) {
            locations = String.join(", ", state.getLocationsUsed());
        }
        criteriaLabel.setText("Matching: " + String.join(", ", state.getTopicsUsed())
                + "  |  Locations: " + locations);
    }

    /**
     * Redraws the list of professors and the error or count message.
     *
     * @param state the current state
     */
    private void updateResults(RankedProfessorState state) {
        resultsPanel.removeAll();

        final String error = state.getError();
        if (error != null && !error.isBlank()) {
            errorLabel.setText(error);
            messageLabel.setText(" ");
        }
        else {
            errorLabel.setText(" ");
            if (state.getProfessors().isEmpty()) {
                messageLabel.setText("Pick some topics and press Search.");
            }
            else {
                messageLabel.setText(state.getProfessors().size() + " professors found");
            }
        }

        for (RankedProfessorEntry entry : state.getProfessors()) {
            resultsPanel.add(makeProfessorRow(entry));
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    /**
     * Shows the home page for a user and loads the list from their profile.
     * Call this after login succeeds.
     *
     * @param loggedInUsername the user who just logged in
     */
    public void loadFor(String loggedInUsername) {
        this.username = loggedInUsername;
        runSearch(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Returns the name used to switch to this view.
     *
     * @return the view name
     */
    public String getViewName() {
        return RankedProfessorViewModel.VIEW_NAME;
    }

    /**
     * Sets the controller that this view calls to search.
     *
     * @param rankedProfessorController the ranked professor controller
     */
    public void setRankedProfessorController(RankedProfessorController rankedProfessorController) {
        this.rankedProfessorController = rankedProfessorController;
    }

    /**
     * Sets the controller used to open a professor's page.
     *
     * @param professorPageController the professor page controller
     */
    public void setProfessorPageController(ProfessorPageController professorPageController) {
        this.professorPageController = professorPageController;
    }
}