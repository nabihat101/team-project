package use_case.EmailGeneration;

import entities.User;
import entities.Professor;

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
}
