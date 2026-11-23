package use_case.EmailGeneration;

import entities.User;
import entities.Professor;

import java.util.List;

public interface EmailGenerationDataAccessInterface {

    /**
     *
     * @param professorID the ID of the currently viewed professor
     * @return the corresponding Professor object
     */
    Professor getProfessorByID(String professorID);

    /**
     *
     * @param username the username of the user
     * @return the corresponding User object
     */
    User getUserByUsername(String username);

    /**
     *
     * @param context professor and user information
     * @param instruction instruction for generate an email
     * @return the email subject and body
     */
    List<String> generateEmail(String context, String instruction);
}
