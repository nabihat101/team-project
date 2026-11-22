package use_case.UserProfile;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;

import java.util.List;

public class UserProfileOutputData {

    private final User user;
    private final List<ResearchPaper> savedPapers;
    private final List<ContactedProfessor> contactedProfessors;

    /**
     *
     * @param user                  The user object
     * @param savedPapers           List of the user's saved papers
     * @param contactedProfessors   List of the professors the user has contacted
     */
    public UserProfileOutputData(User user, List<ResearchPaper> savedPapers,
                                 List<ContactedProfessor> contactedProfessors){

        this.user = user;
        this.savedPapers = savedPapers;
        this.contactedProfessors = contactedProfessors;

    }

    /**
     *
     * @return the user object
     */
    public User getUser(){
        return user;
    }

    /**
     *
     * @return the papers the user has saved
     */
    public List<ResearchPaper> getSavedPapers(){
        return savedPapers;
    }

    /**
     *
     * @return the professors the user has contacted
     */
    public List<ContactedProfessor> getContactedProfessors(){
        return contactedProfessors;
    }


}
