package LoginUseCase;

import entities.User;

public interface LoginDataAccessInterface {

    /**
     * Checks if the username already exists
     * @param username the username to look for
     * @return True if a user with the same username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Saves the user
     * @param user the user object to save
     */
    void save(User user);

    /**
     * Returns the user with the given username
     * @param username the username to look up
     * @return the user object with the given username
     */
    User get(String username);

    /**
     * Set the current username with the given username
     * @param username the username to set
     */
    void setUsername(String username);

    /**
     * Get the current username
     * @return the username of the logged-in user
     */
    String getUsername();

}
