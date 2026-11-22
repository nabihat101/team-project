package use_case.UserProfile;

import java.util.List;

public interface UserProfileInputBoundary {

    void execute (UserProfileInputData inputData);

    void updateContactResponseStatus(String username, String professorName,
                                     String institution, boolean responded);

    void updateUserPreferences(String username, List<String> interests,
                               List<String> institutions);

    void removeSavedPaper(String username, String paperId);

}
