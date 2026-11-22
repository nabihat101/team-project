package use_case.UserProfile;

import java.util.List;

public interface UserProfileInputBoundary {

    void execute (UserProfileInputData inputData);

    //void updateContactResponseStatus(UpdateContactResponseInputData inputData);

    void updateUserPreferences(String username, List<String> interests,
                               List<String> institutions);

    void removeSavedPaper(String username, String paperId);

}
