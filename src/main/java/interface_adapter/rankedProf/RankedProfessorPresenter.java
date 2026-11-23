package interface_adapter.rankedProf;

import use_case.RankedProfessorCase.RankedProfessorOutputBoundary;
import use_case.RankedProfessorCase.RankedProfessorOutputData;

import java.util.ArrayList;

/** Presenter for the ranked professors use case. */
public class RankedProfessorPresenter implements RankedProfessorOutputBoundary {

    private final RankedProfessorViewModel viewModel;

    public RankedProfessorPresenter(RankedProfessorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RankedProfessorOutputData outputData) {
        RankedProfessorState state = viewModel.getState();
        state.setProfessors(outputData.getProfessors());
        state.setTopicsUsed(outputData.getTopicsUsed());
        state.setLocationsUsed(outputData.getLocationsUsed());
        state.setError(null);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        RankedProfessorState state = viewModel.getState();
        state.setProfessors(new ArrayList<>());   // clear the old list so stale results don't stay visible
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}