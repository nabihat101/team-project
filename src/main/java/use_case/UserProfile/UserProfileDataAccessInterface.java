package use_case.UserProfile;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;

import java.util.List;

public interface UserProfileDataAccessInterface {

    /**
     *
     * @param username      User's username
     * @return              The user with the given username
     */
    User getUser(String username);

    /**
     *
     * @param username      User's username
     * @return              List of the user's saved research papers
     */
    List<ResearchPaper> getSavedPapers(String username);

    /**
     *
     * @param username      User's username
     * @return              List of the user's contacted professors
     */
    List<ContactedProfessor> getContactedProfessors(String username);

    /**
     *
     * @param username       User's username
     * @param paperId       The ID of the paper to be removed from the saved list
     * @return              Boolean showing whether removal was successful or not
     */
    boolean removeSavedPaper(String username, String paperId);

    /**
     *
     * @param username          User's username
     * @param interests         User's updated interests
     * @param institutions      User's updated preferred institutions
     * @return                  boolean of whether the update was successful or not
     */
    boolean updateUserPreferences(String username, List<String> interests, List<String> institutions);

    /**
     *
     * @param username          User's username
     * @param professorName     Professor's name
     * @param institution       Insitutuion of the professor
     * @param responded         Whether they have responded or not
     * @return                  ContactedProfessor object to represent the change in responded status
     */
        ContactedProfessor updateContactResponseStatus(String username, String professorName,
                                                        String institution, boolean responded);
}


