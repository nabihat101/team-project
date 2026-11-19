package view;

import entities.ResearchPaper;
import interface_adapter.professor_pages.ProfessorPageController;
import interface_adapter.professor_pages.ProfessorPageState;
import interface_adapter.professor_pages.ProfessorPageViewModel;
import interface_adapter.professor_pages.ProfessorPageViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Displays professor's information or error message
 */
public class ProfessorPageView extends JPanel implements ProfessorPageViewModelListener{
    private final String viewName = "professor page";
    private final ProfessorPageViewModel professorPageViewModel;

    private ProfessorPageController professorPageController;

    private final JLabel nameLabel = new JLabel();
    private final JLabel institutionLabel = new JLabel();
    private final JLabel researchAreasLabel = new JLabel();
    private final JLabel errorLabel = new JLabel(" ");

    private final JPanel publicationsPanel = new JPanel();

    /**
     * Init. all fields
     * @param professorPageViewModel
     */
    public ProfessorPageView(ProfessorPageViewModel professorPageViewModel) {
        this.professorPageViewModel = professorPageViewModel;
        this.professorPageViewModel.addListener(this);

        setLayout(new BorderLayout());

        JPanel professorInfoPanel = new JPanel();
        professorInfoPanel.setLayout(new BoxLayout(professorInfoPanel, BoxLayout.Y_AXIS));

        professorInfoPanel.add(nameLabel);
        professorInfoPanel.add(institutionLabel);
        professorInfoPanel.add(researchAreasLabel);
        professorInfoPanel.add(errorLabel);

        publicationsPanel.setLayout(new BoxLayout(publicationsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(publicationsPanel);

        add(professorInfoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Updates view when presenter changes viewModel
     */
    @Override
    public void stateChanged() {
        ProfessorPageState state = professorPageViewModel.getState();

        publicationsPanel.removeAll();

        if (state.getProfessor() != null) {
            nameLabel.setText(state.getProfessor().getName());

            institutionLabel.setText(state.getProfessor().getInstitution());

            researchAreasLabel.setText(
                    "Research Areas: " +
                            String.join(", ", state.getProfessor().getResearchAreas())
            );

            for (ResearchPaper paper : state.getProfessor().getPublications()) {
                JLabel titleLabel = new JLabel(
                        paper.getTitle() + " (" + paper.getYear() + ")"
                );

                JButton urlButton = new JButton("View Paper");

                urlButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(paper.getUrl()));
                    } catch (Exception ex) {
                        errorLabel.setText("Unable to open paper URL.");
                    }
                });

                publicationsPanel.add(titleLabel);
                publicationsPanel.add(urlButton);
            }
        }
         if (state.getError() != null) {
            nameLabel.setText(" ");
            institutionLabel.setText(" ");
            researchAreasLabel.setText(" ");
            errorLabel.setText(state.getError());
        } else {
            errorLabel.setText(" ");
        }

        publicationsPanel.revalidate();
        publicationsPanel.repaint();
    }

    /**
     * Gets view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets controller for use case
     */
    public void setProfessorPageController(ProfessorPageController professorPageController) {
        this.professorPageController = professorPageController;
    }
}
