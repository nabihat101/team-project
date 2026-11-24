package view;

import interface_adapter.PaperOfTheDay.PaperOfTheDayController;
import interface_adapter.PaperOfTheDay.PaperOfTheDayState;
import interface_adapter.PaperOfTheDay.PaperOfTheDayViewModel;
import interface_adapter.PaperOfTheDay.PaperOfTheDayViewModelListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Shows today's recommended paper: title, authors, why it was picked, the abstract,
 * and a button to open the full paper.
 *
 * Note: the UI layout and styling in this file were generated with AI assistance
 * (Claude) and then reviewed and adapted to match our LoginView.
 */
public class PaperOfTheDayView extends JPanel implements PaperOfTheDayViewModelListener {

    // same colours and font as LoginView so the app looks consistent
    private static final Color NAVY = new Color(0x0F1B2D);
    private static final Color BLUE = new Color(0x1D4ED8);
    private static final Color GRAY = new Color(0x5B667E);
    private static final Color LIGHT_GRAY = new Color(0xC9CFDD);
    private static final Color PAGE_BG = new Color(0xF5F7FB);
    private static final Color CHIP_BG = new Color(0xE0E7FF);
    private static final Color ERROR_RED = new Color(0xB91C1C);
    private static final String FONT = "SansSerif";

    // how wide the title can get before it wraps onto the next line
    private static final int TITLE_WIDTH = 560;

    private final String viewName = PaperOfTheDayViewModel.VIEW_NAME;
    private final PaperOfTheDayViewModel paperOfTheDayViewModel;
    private PaperOfTheDayController paperOfTheDayController;

    private final JLabel titleLabel = new JLabel();
    private final JLabel authorsLabel = new JLabel();
    private final JPanel reasonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
    private final JTextArea abstractArea = new JTextArea();
    private final JButton readButton = new JButton("Read paper");
    private final JLabel statusLabel = new JLabel("Loading today's paper...");
    private final JPanel card = new JPanel();

    private String currentUrl = "";

    /**
     * Creates the Paper of the Day view and listens for view model changes.
     *
     * @param paperOfTheDayViewModel the view model that holds the screen's state
     */
    public PaperOfTheDayView(PaperOfTheDayViewModel paperOfTheDayViewModel) {
        this.paperOfTheDayViewModel = paperOfTheDayViewModel;
        this.paperOfTheDayViewModel.addListener(this);

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        setPreferredSize(new Dimension(900, 560));

        add(makeHeader(), BorderLayout.NORTH);
        add(makeCard(), BorderLayout.CENTER);

        readButton.addActionListener(evt -> openPaper());

        // nothing to show until the presenter sends a paper
        card.setVisible(false);
    }

    /**
     * Makes the "PAPER OF THE DAY · date" header at the top.
     *
     * @return the header panel
     */
    private JPanel makeHeader() {
        final JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d"));
        final JLabel kicker = new JLabel("PAPER OF THE DAY  ·  " + today.toUpperCase());
        kicker.setFont(new Font(FONT, Font.BOLD, 12));
        kicker.setForeground(BLUE);

        statusLabel.setFont(new Font(FONT, Font.PLAIN, 14));
        statusLabel.setForeground(GRAY);

        kicker.setAlignmentX(LEFT_ALIGNMENT);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        header.add(kicker);
        header.add(Box.createVerticalStrut(8));
        header.add(statusLabel);
        header.add(Box.createVerticalStrut(16));
        return header;
    }

    /**
     * Makes the white card that holds the paper.
     *
     * @return the card panel
     */
    private JPanel makeCard() {
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                BorderFactory.createEmptyBorder(28, 32, 28, 32)));

        titleLabel.setFont(new Font(FONT, Font.BOLD, 24));
        titleLabel.setForeground(NAVY);

        authorsLabel.setFont(new Font(FONT, Font.PLAIN, 14));
        authorsLabel.setForeground(GRAY);

        reasonsRow.setOpaque(false);
        // without a maximum height, BoxLayout can stretch the row too tall
        reasonsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        abstractArea.setFont(new Font(FONT, Font.PLAIN, 14));
        abstractArea.setForeground(NAVY);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        abstractArea.setEditable(false);
        abstractArea.setOpaque(false);
        abstractArea.setBorder(null);

        final JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setBorder(null);
        abstractScroll.setOpaque(false);
        abstractScroll.getViewport().setOpaque(false);

        readButton.setFont(new Font(FONT, Font.BOLD, 15));
        readButton.setBackground(BLUE);
        readButton.setForeground(Color.WHITE);
        readButton.setOpaque(true);
        readButton.setBorderPainted(false);
        readButton.setFocusPainted(false);
        readButton.setPreferredSize(new Dimension(160, 42));
        readButton.setMaximumSize(new Dimension(160, 42));
        readButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        authorsLabel.setAlignmentX(LEFT_ALIGNMENT);
        reasonsRow.setAlignmentX(LEFT_ALIGNMENT);
        abstractScroll.setAlignmentX(LEFT_ALIGNMENT);
        readButton.setAlignmentX(LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(authorsLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(reasonsRow);
        card.add(Box.createVerticalStrut(18));
        card.add(makeSectionLabel("Abstract"));
        card.add(Box.createVerticalStrut(6));
        card.add(abstractScroll);
        card.add(Box.createVerticalStrut(18));
        card.add(readButton);
        return card;
    }

    /**
     * Makes a small bold gray label, like the ones above the login fields.
     *
     * @param text the label text
     * @return the label
     */
    private JLabel makeSectionLabel(String text) {
        final JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font(FONT, Font.BOLD, 12));
        label.setForeground(GRAY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Makes a light blue pill for one "why it was picked" reason.
     *
     * @param text the reason
     * @return the chip label
     */
    private JLabel makeChip(String text) {
        final JLabel chip = new JLabel(text);
        chip.setFont(new Font(FONT, Font.BOLD, 12));
        chip.setForeground(BLUE);
        chip.setBackground(CHIP_BG);
        chip.setOpaque(true);
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return chip;
    }

    /**
     * Opens the paper's link in the user's browser.
     */
    private void openPaper() {
        if (currentUrl == null || currentUrl.isBlank()) {
            statusLabel.setText("This paper doesn't have a link.");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(currentUrl));
        }
        catch (Exception ex) {
            statusLabel.setText("Unable to open the paper link.");
        }
    }

    /**
     * Updates the screen when the view model's state changes.
     */
    @Override
    public void stateChanged() {
        final PaperOfTheDayState state = paperOfTheDayViewModel.getState();
        final String error = state.getError();

        // something went wrong: show the message, hide the card
        if (error != null && !error.isBlank()) {
            statusLabel.setText(error);
            statusLabel.setForeground(ERROR_RED);
            card.setVisible(false);
            revalidate();
            repaint();
            return;
        }

        statusLabel.setText("Picked for you today.");
        statusLabel.setForeground(GRAY);

        // html lets a long title wrap instead of getting cut off
        titleLabel.setText("<html><div style='width:" + TITLE_WIDTH + "px'>"
                + escapeHtml(state.getTitle()) + "</div></html>");

        authorsLabel.setText(formatAuthors(state.getAuthors()) + "  ·  " + state.getYear());

        reasonsRow.removeAll();
        for (String reason : state.getReasons()) {
            reasonsRow.add(makeChip(reason));
        }

        final String abstractText = state.getAbstractText();
        if (abstractText == null || abstractText.isBlank()) {
            abstractArea.setText("No abstract available.");
            abstractArea.setForeground(GRAY);
        }
        else {
            abstractArea.setText(abstractText);
            abstractArea.setForeground(NAVY);
        }
        abstractArea.setCaretPosition(0);

        currentUrl = state.getUrl();
        readButton.setEnabled(currentUrl != null && !currentUrl.isBlank());

        card.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Shows up to three authors, then "et al." so long author lists don't overflow.
     *
     * @param authors the paper's authors
     * @return the authors as one line
     */
    private String formatAuthors(List<String> authors) {
        if (authors == null || authors.isEmpty()) {
            return "Unknown authors";
        }
        if (authors.size() <= 3) {
            return String.join(", ", authors);
        }
        return String.join(", ", authors.subList(0, 3)) + " et al.";
    }

    /**
     * Stops characters like < and & in a title from breaking the html.
     *
     * @param text the raw text
     * @return the text with html characters escaped
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Returns the name used to switch to this view.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the controller this view uses to load the paper.
     *
     * @param paperOfTheDayController the Paper of the Day controller
     */
    public void setPaperOfTheDayController(PaperOfTheDayController paperOfTheDayController) {
        this.paperOfTheDayController = paperOfTheDayController;
    }

    /**
     * Asks for today's paper for the given user. Call this right after they log in.
     *
     * @param username the logged-in user's username
     */
    public void loadFor(String username) {
        statusLabel.setText("Loading today's paper...");
        statusLabel.setForeground(GRAY);
        if (paperOfTheDayController != null) {
            paperOfTheDayController.execute(username);
        }
    }
}