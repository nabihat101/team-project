package use_case.RankedProfessorCase;

import entities.User;

public interface UserDataAccessInterface {
    /** Returns the user with this username, so we can read their interests and locations. */
    User get(String username);
}