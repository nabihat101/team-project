package interface_adapter.professor_pages;

import use_case.professor_pages.ProfessorPageOutputBoundary;
import use_case.professor_pages.ProfessorPageOutputData;

public class ProfessorPagePresenter implements ProfessorPageOutputBoundary {
    private final ProfessorPageViewModel professorPageViewModel;

    /**
     * Init. all fields
     * @param viewModel - ProfessorPageViewModel
     */
    public ProfessorPagePresenter(ProfessorPageViewModel viewModel) {
        this.professorPageViewModel = viewModel;
    }

    /**
     * Sets success view
     * @param outputData - Object
     */
    public void prepareSuccessView(ProfessorPageOutputData outputData) {
        ProfessorPageState state = professorPageViewModel.getState();

        state.setProfessor(outputData.getProfessor());
        state.setError(null);

        professorPageViewModel.setState(state);
        professorPageViewModel.notifyStateChanged();

    }

    /**
     * Prepares fail view
     * @param error - error message
     */
    public void prepareFailView(String error) {
        ProfessorPageState state = professorPageViewModel.getState();

        state.setError(error);

        professorPageViewModel.setState(state);
        professorPageViewModel.notifyStateChanged();
    }
}
