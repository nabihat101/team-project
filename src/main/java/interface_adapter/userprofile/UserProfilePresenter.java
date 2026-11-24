package interface_adapter.userprofile;

import entities.ContactedProfessor;
import interface_adapter.ViewManagerModel;
import use_case.UserProfile.UserProfileOutputBoundary;
import use_case.UserProfile.UserProfileOutputData;

public class UserProfilePresenter implements UserProfileOutputBoundary {

    private final UserProfileViewModel userProfileViewModel;

    public UserProfilePresenter(UserProfileViewModel userProfileViewModel) {
        this.userProfileViewModel = userProfileViewModel;
    }

    /**
     * @param outputData
     */
    @Override
    public void prepareSuccessView(UserProfileOutputData outputData) {
        UserProfileState state = new UserProfileState();
        state.setUser(outputData.getUser());
        state.setSavedPapers(outputData.getSavedPapers());
        state.setContactedProfessors(outputData.getContactedProfessors());
        userProfileViewModel.setState(state);
        userProfileViewModel.notifyStateChanged();

    }

    /**
     * @param updatedProfessor
     */
    @Override
    public void prepareContactResponseUpdated(ContactedProfessor updatedProfessor) {
        final UserProfileState state = userProfileViewModel.getState();

        // Replace the matching professor with the updated response
         for (int i = 0; i < state.getContactedProfessors().size(); i++) {
            final ContactedProfessor professor = state.getContactedProfessors().get(i);
            if (professor.getName().equals(updatedProfessor.getName())
                    && professor.getInstitution().equals(updatedProfessor.getInstitution())) {
                state.getContactedProfessors().set(i, updatedProfessor);
            }
        }
        state.setError(null);
        userProfileViewModel.setState(state);
        userProfileViewModel.notifyStateChanged();

    }

    /**
     * @param error
     */
    @Override
    public void prepareFailureView(String error) {
        final UserProfileState state = new UserProfileState();
        state.setError(error);
        userProfileViewModel.setState(state);
        userProfileViewModel.notifyStateChanged();
    }
}
