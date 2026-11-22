package view;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.loggedin.LoggedInViewModel;
import interface_adapter.userprofile.UserProfileState;
import interface_adapter.userprofile.UserProfileController;
import interface_adapter.userprofile.UserProfileViewModel;
import interface_adapter.userprofile.UserProfileViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserProfileView extends JPanel implements UserProfileViewModelListener {
    private static final Color NAVY = new Color(0x0F1B2D);
    private static final Color BLUE = new Color(0x1D4ED8);
    private static final Color GRAY = new Color(0x5B667E);
    private static final Color LIGHT_GRAY = new Color(0xC9CFDD);
    private static final Color BACKGROUND = new Color(0xF4F6FA);
    private static final Color SOFT_BLUE = new Color(0xF3F7FF);
    private static final String FONT = "SansSerif";

    private final UserProfileViewModel userProfileViewModel;
    private final ViewManagerModel viewManagerModel;
    private final JPanel content = new JPanel();
    private final JPanel activityCards = new JPanel(new BorderLayout());
    private final JTextField activitySearchField = new JTextField();
    private boolean papersSortedByTitle;
    private boolean professorsSortedByName;
    private UserProfileController userProfileController;

    public UserProfileView(UserProfileViewModel userProfileViewModel,
                           ViewManagerModel viewManagerModel) {
        this.userProfileViewModel = userProfileViewModel;
        this.viewManagerModel = viewManagerModel;
        userProfileViewModel.addListener(this);
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(900, 560));

        add(makeHeader(), BorderLayout.NORTH);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(24, 32, 32, 32));
        final JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        activitySearchField.setFont(new Font(FONT, Font.PLAIN, 13));
        activitySearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        activitySearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                renderActivityCards();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                renderActivityCards();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                renderActivityCards();
            }
        });
    }

    public void setUserProfileController(UserProfileController userProfileController) {
        this.userProfileController = userProfileController;
    }

    private JPanel makeHeader() {
        final JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));

        final JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        final JLabel eyebrow = new JLabel("CS RESEARCH FINDER");
        eyebrow.setFont(new Font(FONT, Font.BOLD, 11));
        eyebrow.setForeground(new Color(0xAFC3F8));
        eyebrow.setAlignmentX(LEFT_ALIGNMENT);
        final JLabel title = new JLabel("Your research profile");
        title.setFont(new Font(FONT, Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        final JLabel subtitle = new JLabel("Your research interests, reading list, and outreach.");
        subtitle.setFont(new Font(FONT, Font.PLAIN, 13));
        subtitle.setForeground(new Color(0xC3CCE0));
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        heading.add(eyebrow);
        heading.add(Box.createVerticalStrut(6));
        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitle);
        header.add(heading, BorderLayout.WEST);

        final JButton backButton = new JButton("Back to home");
        backButton.setFont(new Font(FONT, Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x243650));
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x52627A)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(event ->
                viewManagerModel.setCurrentViewName(LoggedInViewModel.VIEW_NAME));
        header.add(backButton, BorderLayout.EAST);
        return header;
    }

    @Override
    public void stateChanged() {
        content.removeAll();
        final UserProfileState state = userProfileViewModel.getState();
        if (state.getError() != null) {
            final JPanel errorCard = makeCard();
            errorCard.add(makeMetadata(state.getError()));
            content.add(errorCard);
        } else if (state.getUser() != null) {
            content.add(makeAccountCard(state.getUser()));
            content.add(Box.createVerticalStrut(14));
            content.add(makeSummary(state));
            content.add(Box.createVerticalStrut(26));
            content.add(makeActivitySearch());
            content.add(Box.createVerticalStrut(14));
            activityCards.setOpaque(false);
            content.add(activityCards);
            renderActivityCards();
        }
        content.revalidate();
        content.repaint();
    }

    private JPanel makeAccountCard(User user) {
        final JPanel card = makeCard();
        card.setLayout(new BorderLayout());
        final JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        final JPanel identity = new JPanel(new BorderLayout(14, 0));
        identity.setOpaque(false);
        final JLabel avatar = new JLabel(initials(user.getUsername()), SwingConstants.CENTER);
        avatar.setFont(new Font(FONT, Font.BOLD, 15));
        avatar.setForeground(BLUE);
        avatar.setBackground(new Color(0xE7EEFF));
        avatar.setOpaque(true);
        avatar.setPreferredSize(new Dimension(48, 48));
        identity.add(avatar, BorderLayout.WEST);

        final JPanel identityText = new JPanel();
        identityText.setOpaque(false);
        identityText.setLayout(new BoxLayout(identityText, BoxLayout.Y_AXIS));
        final JLabel name = new JLabel(user.getUsername());
        name.setFont(new Font(FONT, Font.BOLD, 19));
        name.setForeground(NAVY);
        final JLabel caption = new JLabel("Researcher account · Profile preferences");
        caption.setFont(new Font(FONT, Font.PLAIN, 13));
        caption.setForeground(GRAY);
        identityText.add(name);
        identityText.add(Box.createVerticalStrut(4));
        identityText.add(caption);
        identity.add(identityText, BorderLayout.CENTER);

        final JButton editPreferencesButton = new JButton("Edit interests");
        editPreferencesButton.setFont(new Font(FONT, Font.BOLD, 12));
        editPreferencesButton.setForeground(BLUE);
        editPreferencesButton.setBackground(Color.WHITE);
        editPreferencesButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xC9D7F5)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        editPreferencesButton.setFocusPainted(false);
        editPreferencesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editPreferencesButton.addActionListener(event -> editPreferences(user));
        identity.add(editPreferencesButton, BorderLayout.EAST);

        final JPanel details = new JPanel(new GridLayout(1, 2, 12, 0));
        details.setOpaque(false);
        details.add(makeDetail("RESEARCH INTERESTS", formatList(user.getInterests())));
        details.add(makeDetail("PREFERRED INSTITUTIONS", formatList(user.getInstitutions())));
        body.add(identity);
        body.add(Box.createVerticalStrut(18));
        body.add(details);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel makeDetail(String title, String value) {
        final JPanel detail = new JPanel(new BorderLayout(0, 6));
        detail.setBackground(SOFT_BLUE);
        detail.setBorder(BorderFactory.createEmptyBorder(11, 13, 11, 13));
        final JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 10));
        label.setForeground(GRAY);
        final JLabel text = new JLabel("<html><div style='width:300px'>"
                + escapeHtml(value) + "</div></html>");
        text.setFont(new Font(FONT, Font.PLAIN, 14));
        text.setForeground(NAVY);
        detail.add(label, BorderLayout.NORTH);
        detail.add(text, BorderLayout.CENTER);
        return detail;
    }

    private JPanel makeSummary(UserProfileState state) {
        final JPanel summary = new JPanel(new GridLayout(1, 3, 12, 0));
        summary.setOpaque(false);
        summary.setMaximumSize(new Dimension(1100, 62));
        final long responses = state.getContactedProfessors().stream()
                .filter(ContactedProfessor::hasResponded).count();
        summary.add(makeSummaryTile(state.getSavedPapers().size(), "SAVED PAPERS"));
        summary.add(makeSummaryTile(state.getContactedProfessors().size(),
                "PROFESSORS CONTACTED"));
        summary.add(makeSummaryTile((int) responses, "RESPONSES RECEIVED"));
        return summary;
    }

    private JPanel makeSummaryTile(int count, String caption) {
        final JPanel tile = new JPanel(new BorderLayout(10, 0));
        tile.setBackground(Color.WHITE);
        tile.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE4EAF2)),
                BorderFactory.createEmptyBorder(11, 14, 11, 14)));
        final JLabel number = new JLabel(Integer.toString(count));
        number.setFont(new Font(FONT, Font.BOLD, 20));
        number.setForeground(BLUE);
        final JLabel label = new JLabel(caption);
        label.setFont(new Font(FONT, Font.BOLD, 10));
        label.setForeground(GRAY);
        tile.add(number, BorderLayout.WEST);
        tile.add(label, BorderLayout.CENTER);
        return tile;
    }

    private JPanel makeActivitySearch() {
        final JPanel searchPanel = new JPanel(new BorderLayout(20, 0));
        searchPanel.setOpaque(false);
        final JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        final JLabel title = new JLabel("Your activity");
        title.setFont(new Font(FONT, Font.BOLD, 19));
        title.setForeground(NAVY);
        final JLabel subtitle = new JLabel("Keep track of what you read and who you reach out to.");
        subtitle.setFont(new Font(FONT, Font.PLAIN, 12));
        subtitle.setForeground(GRAY);
        heading.add(title);
        heading.add(Box.createVerticalStrut(3));
        heading.add(subtitle);
        searchPanel.add(heading, BorderLayout.WEST);
        activitySearchField.setToolTipText("Search papers and contacted professors");
        activitySearchField.setPreferredSize(new Dimension(250, 38));
        searchPanel.add(activitySearchField, BorderLayout.EAST);
        searchPanel.setMaximumSize(new Dimension(1100, 48));
        return searchPanel;
    }

    private void renderActivityCards() {
        final UserProfileState state = userProfileViewModel.getState();
        if (state.getUser() == null) {
            return;
        }
        final String query = activitySearchField.getText().trim().toLowerCase();
        final List<ResearchPaper> papers = state.getSavedPapers().stream()
                .filter(paper -> matchesPaper(paper, query))
                .collect(java.util.stream.Collectors.toList());
        final List<ContactedProfessor> professors = state.getContactedProfessors().stream()
                .filter(professor -> matchesProfessor(professor, query))
                .collect(java.util.stream.Collectors.toList());

        final List<ResearchPaper> sortedPapers = new ArrayList<>(papers);
        if (papersSortedByTitle) {
            sortedPapers.sort(Comparator.comparing(ResearchPaper::getTitle,
                    String.CASE_INSENSITIVE_ORDER));
        }
        final List<ContactedProfessor> sortedProfessors = new ArrayList<>(professors);
        if (professorsSortedByName) {
            sortedProfessors.sort(Comparator.comparing(ContactedProfessor::getName,
                    String.CASE_INSENSITIVE_ORDER));
        } else {
            sortedProfessors.sort(Comparator.comparing(ContactedProfessor::getContactedOn)
                    .reversed());
        }

        final JPanel row = new JPanel(new GridLayout(1, 2, 18, 0));
        row.setOpaque(false);
        row.setAlignmentX(CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE));
        row.add(makeSavedPapersCard(sortedPapers,
                !state.getSavedPapers().isEmpty() && papers.isEmpty() && !query.isEmpty(),
                state.getUser().getUsername()));
        row.add(makeContactedProfessorsCard(professors,
                !state.getContactedProfessors().isEmpty() && professors.isEmpty()
                        && !query.isEmpty(),
                state.getUser().getUsername(), sortedProfessors));
        activityCards.removeAll();
        activityCards.add(row, BorderLayout.CENTER);
        activityCards.revalidate();
        activityCards.repaint();
    }

    private boolean matchesPaper(ResearchPaper paper, String query) {
        if (query.isEmpty()) {
            return true;
        }
        final String searchable = paper.getTitle() + " "
                + String.join(" ", paper.getAuthors()) + " "
                + paper.getAbstractText() + " " + paper.getYear();
        return searchable.toLowerCase().contains(query);
    }

    private boolean matchesProfessor(ContactedProfessor professor, String query) {
        if (query.isEmpty()) {
            return true;
        }
        final String response = professor.hasResponded() ? "responded" : "awaiting response";
        final String searchable = professor.getName() + " " + professor.getInstitution()
                + " " + professor.getContactedOn() + " " + response;
        return searchable.toLowerCase().contains(query);
    }

    private JPanel makeSavedPapersCard(List<ResearchPaper> papers, boolean noMatches,
                                       String username) {
        final JPanel card = makeCard();
        addSectionHeading(card, "Saved papers", papers.size(),
                new String[]{"Recently saved", "Title A-Z"}, papersSortedByTitle,
                selected -> {
                    papersSortedByTitle = "Title A-Z".equals(selected);
                    renderActivityCards();
                });
        if (papers.isEmpty()) {
            card.add(makeEmptyMessage(noMatches
                    ? "No saved papers match your search."
                    : "No saved papers yet. Browse professors' work to start "
                    + "building your reading list."));
        } else {
            for (ResearchPaper paper : papers) {
                final JButton paperButton = makeLinkButton(paper.getTitle());
                paperButton.addActionListener(event -> openPaper(paper));
                final JPanel paperRow = new JPanel(new BorderLayout(8, 0));
                paperRow.setOpaque(false);
                paperRow.add(paperButton, BorderLayout.CENTER);
                final JButton removeButton = new JButton("Remove");
                removeButton.setFont(new Font(FONT, Font.PLAIN, 11));
                removeButton.setForeground(new Color(0xB91C1C));
                removeButton.setBorderPainted(false);
                removeButton.setContentAreaFilled(false);
                removeButton.setFocusPainted(false);
                removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                removeButton.addActionListener(event -> removeSavedPaper(username, paper));
                paperRow.add(removeButton, BorderLayout.EAST);
                card.add(paperRow);
                final String metadata = paper.getYear() > 0
                        ? String.join(", ", paper.getAuthors()) + " · " + paper.getYear()
                        : String.join(", ", paper.getAuthors());
                card.add(makeMetadata(metadata.isBlank() ? "Research paper" : metadata));
                card.add(Box.createVerticalStrut(12));
            }
        }
        return card;
    }

    private JPanel makeContactedProfessorsCard(List<ContactedProfessor> professors,
                                               boolean noMatches, String username,
                                               List<ContactedProfessor> sortedProfessors) {
        final JPanel card = makeCard();
        addSectionHeading(card, "Contacted professors", professors.size(),
                new String[]{"Most recent", "Name A-Z"}, professorsSortedByName,
                selected -> {
                    professorsSortedByName = "Name A-Z".equals(selected);
                    renderActivityCards();
                });
        if (professors.isEmpty()) {
            card.add(makeEmptyMessage(noMatches
                    ? "No contacted professors match your search."
                    : "No professors marked as contacted yet. They’ll appear "
                    + "here when you reach out."));
        } else {
            for (ContactedProfessor professor : sortedProfessors) {
                final JButton professorButton = makeLinkButton(professor.getName());
                professorButton.addActionListener(event -> showProfessor(professor));
                card.add(professorButton);
                card.add(makeMetadata(professor.getInstitution()));
                card.add(makeStatus(professor, username));
                card.add(Box.createVerticalStrut(10));
            }
        }
        return card;
    }

    private JPanel makeCard() {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE1E7F0)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        card.setAlignmentX(CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE));
        return card;
    }

    private void addSectionHeading(JPanel card, String title, int count,
                                   String[] sortOptions, boolean sortedAlphabetically,
                                   java.util.function.Consumer<String> sortAction) {
        final JPanel heading = new JPanel(new BorderLayout(8, 0));
        heading.setOpaque(false);
        final JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 15));
        label.setForeground(NAVY);
        final JLabel badge = new JLabel(Integer.toString(count), JLabel.CENTER);
        badge.setFont(new Font(FONT, Font.BOLD, 11));
        badge.setForeground(BLUE);
        badge.setBackground(new Color(0xEFF4FF));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);
        final JComboBox<String> sortSelector = new JComboBox<>(sortOptions);
        sortSelector.setFont(new Font(FONT, Font.PLAIN, 11));
        sortSelector.setSelectedIndex(sortedAlphabetically ? 1 : 0);
        sortSelector.addActionListener(event ->
                sortAction.accept((String) sortSelector.getSelectedItem()));
        controls.add(sortSelector);
        controls.add(badge);
        heading.add(label, BorderLayout.WEST);
        heading.add(controls, BorderLayout.EAST);
        heading.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        card.add(heading);
        card.add(Box.createVerticalStrut(16));
    }

    private JButton makeLinkButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT, Font.BOLD, 13));
        button.setForeground(BLUE);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setMargin(new java.awt.Insets(0, 0, 0, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel makeMetadata(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT, Font.PLAIN, 12));
        label.setForeground(GRAY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private String initials(String username) {
        final String[] words = username.trim().split("\\s+");
        if (words.length == 0 || words[0].isEmpty()) {
            return "?";
        }
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.min(words.length, 2); i++) {
            result.append(words[i].charAt(0));
        }
        return result.toString().toUpperCase();
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private JLabel makeEmptyMessage(String text) {
        final JLabel label = makeMetadata("<html><div style='width:230px'>"
                + text + "</div></html>");
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 4, 0));
        return label;
    }

    private JPanel makeStatus(ContactedProfessor professor, String username) {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        final boolean responded = professor.hasResponded();
        final JLabel status = new JLabel(responded ? "Responded" : "Awaiting response");
        status.setFont(new Font(FONT, Font.BOLD, 10));
        status.setForeground(responded ? new Color(0x166534) : GRAY);
        status.setBackground(responded ? new Color(0xDCFCE7) : new Color(0xEEF1F5));
        status.setOpaque(true);
        status.setBorder(BorderFactory.createEmptyBorder(3, 7, 3, 7));
        row.add(status);
        row.add(Box.createHorizontalStrut(8));
        row.add(makeMetadata("Contacted " + professor.getContactedOn()));
        final JButton responseButton = new JButton(
                responded ? "Undo" : "Mark responded");
        responseButton.setFont(new Font(FONT, Font.PLAIN, 11));
        responseButton.setForeground(BLUE);
        responseButton.setBorderPainted(false);
        responseButton.setContentAreaFilled(false);
        responseButton.setFocusPainted(false);
        responseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        responseButton.addActionListener(event -> {
            if (userProfileController != null) {
                userProfileController.updateContactResponseStatus(username,
                        professor.getName(), professor.getInstitution(), !responded);
            }
        });
        row.add(Box.createHorizontalStrut(8));
        row.add(responseButton);
        return row;
    }

    private String formatList(List<String> values) {
        return values.isEmpty() ? "None added" : String.join(", ", values);
    }

    private void editPreferences(User user) {
        final JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.add(new JLabel("Research interests (comma-separated)"));
        final JTextField interestsField = new JTextField(String.join(", ", user.getInterests()), 30);
        form.add(interestsField);
        form.add(new JLabel("Preferred institutions (comma-separated)"));
        final JTextField institutionsField = new JTextField(
                String.join(", ", user.getInstitutions()), 30);
        form.add(institutionsField);
        final int result = JOptionPane.showConfirmDialog(this, form,
                "Edit profile preferences", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION && userProfileController != null) {
            userProfileController.updateUserPreferences(user.getUsername(),
                    parseCommaSeparated(interestsField.getText()),
                    parseCommaSeparated(institutionsField.getText()));
        }
    }

    private List<String> parseCommaSeparated(String value) {
        final List<String> values = new ArrayList<>();
        for (String item : value.split(",")) {
            final String trimmed = item.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    private void removeSavedPaper(String username, ResearchPaper paper) {
        final int result = JOptionPane.showConfirmDialog(this,
                "Remove \"" + paper.getTitle() + "\" from your saved papers?",
                "Remove saved paper", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION && userProfileController != null) {
            userProfileController.removeSavedPaper(username, paper.getId());
        }
    }

    private void openPaper(ResearchPaper paper) {
        if (paper.getUrl() == null || paper.getUrl().isBlank()) {
            JOptionPane.showMessageDialog(this, "This paper does not have an available link.",
                    "Paper unavailable", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            JOptionPane.showMessageDialog(this, "This device cannot open web links.",
                    "Unable to open paper", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(paper.getUrl()));
        } catch (IOException | URISyntaxException exception) {
            JOptionPane.showMessageDialog(this, "Unable to open the paper link: "
                    + exception.getMessage(), "Unable to open paper", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showProfessor(ContactedProfessor professor) {
        JOptionPane.showMessageDialog(this, professor.getName() + "\n"
                        + professor.getInstitution() + "\nContacted on " + professor.getContactedOn()
                        + "\n" + (professor.hasResponded() ? "Responded" : "No response yet"),
                "Professor", JOptionPane.INFORMATION_MESSAGE);
    }
}
