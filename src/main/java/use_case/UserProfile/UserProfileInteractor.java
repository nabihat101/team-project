package use_case.UserProfile;

import entities.ContactedProfessor;
import entities.User;

import java.util.List;
import java.util.NoSuchElementException;

public class UserProfileInteractor implements UserProfileInputBoundary {

    private final UserProfileDataAccessInterface userProfileDataAccess;
    private final UserProfileOutputBoundary userProfilePresenter;

    public UserProfileInteractor(UserProfileDataAccessInterface userProfileDataAccess,
                                 UserProfileOutputBoundary userProfilePresenter){

        this.userProfileDataAccess = userProfileDataAccess;
        this.userProfilePresenter = userProfilePresenter;

    }


    @Override
    public void execute(UserProfileInputData inputData) {

        final User user = userProfileDataAccess.getUser(inputData.getUsername());
        userProfilePresenter.prepareSuccessView(new UserProfileOutputData(
                user,
                userProfileDataAccess.getSavedPapers(inputData.getUsername()),
                userProfileDataAccess.getContactedProfessors(inputData.getUsername())));
    }

    @Override
    public void updateContactResponseStatus(String username, String professorName, String institution, boolean responded) {
        try{
            ContactedProfessor updatedProfessor = userProfileDataAccess.updateContactResponseStatus(username,
                    professorName, institution, responded);
            userProfilePresenter.prepareContactResponseUpdated(updatedProfessor);

        } catch(NoSuchElementException exception){
            // i dont think this should ever be reached, but just in case
            userProfilePresenter.prepareFailureView(
                    "Unable to update this professor's response status.");
        }
    }

    @Override
    public void updateUserPreferences(String username, List<String> interests, List<String> institutions) {

        // again, shouldn't be reached, but just in case
        if (!userProfileDataAccess.updateUserPreferences(username, interests, institutions)) {
            userProfilePresenter.prepareFailureView("Unable to update your profile.");
            return;
        }
        execute(new UserProfileInputData(username));
    }

    @Override
    public void removeSavedPaper(String username, String paperId) {
        if (!userProfileDataAccess.removeSavedPaper(username, paperId)) {
            userProfilePresenter.prepareFailureView("Unable to remove this paper.");
            return;
        }
        execute(new UserProfileInputData(username));

    }
}
