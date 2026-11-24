package interface_adapter.userprofile;

import use_case.UserProfile.UserProfileInputBoundary;
import use_case.UserProfile.UserProfileInputData;

import java.util.List;

public class UserProfileController {

    private final UserProfileInputBoundary userProfileInteractor;

    /**
     * Creates controller for given user profile input boundary
     * @param userProfileInteractor     boundary handling the profile requests
     */
    public UserProfileController(UserProfileInputBoundary userProfileInteractor){

        this.userProfileInteractor = userProfileInteractor;

    }

    /**
     * Requests the profile for a user
     *
     * @param username the user whose profile should be loaded
     */
    public void execute(String username){
        userProfileInteractor.execute(new UserProfileInputData(username));
    }

    /**
     * Updates whether a contacted professor has responded
     *
     * @param username          the user who contacted the professor
     * @param professorName     the professor's name
     * @param institution       the professor's institution
     * @param responded         boolean of whether the professor has responded
     */
    public void updateContactResponseStatus(String username, String professorName,
                                            String institution, boolean responded) {
        userProfileInteractor.updateContactResponseStatus(username, professorName, institution,
                responded);
    }

    /**
     * Updates the user's research interests and preferred institutions
     *
     * @param username      the user whose preferences are updated
     * @param interests     the user's research interests
     * @param institutions  the user's preferred institutions
     */
    public void updateUserPreferences(String username, List<String> interests,
                                      List<String> institutions) {
        userProfileInteractor.updateUserPreferences(username, interests, institutions);
    }

    /**
     * Removes a saved paper from a user's profile
     *
     * @param username          the user's username
     * @param paperId           ID of the paper to be removed
     */

    public void removeSavedPaper(String username, String paperId) {
        userProfileInteractor.removeSavedPaper(username, paperId);
    }

}
