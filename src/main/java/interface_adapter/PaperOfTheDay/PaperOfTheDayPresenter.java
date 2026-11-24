package interface_adapter.PaperOfTheDay;

import use_case.PaperOfTheDayUseCase.PaperOfTheDayOutputBoundary;
import use_case.PaperOfTheDayUseCase.PaperOfTheDayOutputData;

public class PaperOfTheDayPresenter implements PaperOfTheDayOutputBoundary {
    private final PaperOfTheDayViewModel paperOfTheDayViewModel;

    /**
     * Initalize all fields
     * @param viewModel the Paper Of The Day view model
     */
    public PaperOfTheDayPresenter(PaperOfTheDayViewModel viewModel) {
        this.paperOfTheDayViewModel = viewModel;
    }

    /**
     * Sets success view
     * @param outputData the paper to display
     */
    @Override
    public void prepareSuccessView(PaperOfTheDayOutputData outputData) {
        PaperOfTheDayState state = paperOfTheDayViewModel.getState();

        state.setTitle(outputData.getTitle());
        state.setAuthors(outputData.getAuthors());
        state.setYear(outputData.getYear());
        state.setUrl(outputData.getUrl());
        state.setAbstractText(outputData.getAbstractText());
        state.setReasons(outputData.getReasons());
        state.setError(null);

        paperOfTheDayViewModel.setState(state);
        paperOfTheDayViewModel.notifyStateChanged();


    }

    /**
     * Prepares fail view
     * @param error - error message
     */
    @Override
    public void prepareFailView(String error) {
        PaperOfTheDayState state = paperOfTheDayViewModel.getState();

        state.setError(error);

        paperOfTheDayViewModel.setState(state);
        paperOfTheDayViewModel.notifyStateChanged();
    }
}